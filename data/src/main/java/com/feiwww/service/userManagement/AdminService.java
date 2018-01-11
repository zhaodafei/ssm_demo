package com.feiwww.service.userManagement;

import com.feiwww.dao.userManagement.AdminDao;
import com.feiwww.memcached.MemcachedUtil;
import com.feiwww.model.log.Log;
import com.feiwww.model.userManagement.Admin;
import com.feiwww.redis.RedisHashUtil;
import com.feiwww.redis.RedisStringUtil;
import com.feiwww.rpc.mq.util.ActiveMQP2PUtil;
import com.feiwww.util.CachePrefix;
import com.feiwww.util.DateUtil;
import com.feiwww.util.RedisCacheConstant;
import com.feiwww.vo.userManagement.AdminCacheKey;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 管理员service
 */
@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;

    public boolean register(Admin admin) {
        return adminDao.register(admin);
    }

    public Admin login(String username, String password) {
        return adminDao.login(username, password);
    }

    /***********以下方法是为了测试mybatis中使用xml**********/
    public List<Admin> findAdmin(String username, String password, int start, int limit) {
        return adminDao.findAdmin(username, password, start, limit);
    }

    public Admin insertAdminWithBackId(Admin admin) {
        int record = adminDao.insertAdminWithBackId(admin);
        if (record == 1) {
            return admin;//这时的admin已经被赋予主键了
        }
        return null;
    }

    /************************ guava cache *************************/
    //TODO(疑问：这个缓存怎么知道有没有过期，是不是底层是实现了？？？)
    LoadingCache<String, List<Admin>> adminListCache = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build(new CacheLoader<String, List<Admin>>() {
                @Override
                public List<Admin> load(String username) throws Exception {
                    return adminDao.getUserByName(username);
                }
            });

    public List<Admin> findByUsername(String username) {
        List<Admin> adminList = null;
        try {
            adminList = adminListCache.get(username);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    /************多条件的查询，key为Object（封装了多个条件的VO类）***********/
    LoadingCache<AdminCacheKey, List<Admin>> adminsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .maximumSize(100)
            .build(new CacheLoader<AdminCacheKey, List<Admin>>() {
                @Override
                public List<Admin> load(AdminCacheKey key) throws Exception {
                    return adminDao.findAdmin(key.getUsername(),
                            key.getPassword(),
                            key.getStart(),
                            key.getLimit());
                }
            });


    public List<Admin> findAdminList(String username,
                                     String password,
                                     int start,
                                     int limit) {
        /**
         * 注意：
         * 如果以一个新建的对象做为key的话，因为每次都是新建一个对象，所以这样的话，
         * 实际上每次访问key都是不同的，即每次访问都是重新进行缓存;
         *
         * 但是实际上，我们想要根据对象的属性来判断对象是否相等，只需要根据这些属性重写对象的hashCode与equals方法即可，
         *
         * 所以重写了AdminCacheKey类的hashCode和equals方法，这样，每次访问的话，
         * 就会以每个条件是否相等来判断对象（即key）是否相等了，这一块儿的缓存就会起作用了
         */
        AdminCacheKey cacheKey = new AdminCacheKey(username,
                password,
                start,
                limit);
        List<Admin> adminList = null;
        try {
            adminList = adminsCache.get(cacheKey);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    /*********************memcached********************/
    public Admin findAdminById(int id) {
        //从缓存中获取数据
        Admin admin = (Admin) MemcachedUtil.getCache(CachePrefix.USER_MANAGEMENT, String.valueOf(id));
        //若缓存中有，直接返回
        if (admin != null) {
            return admin;
        }
        //若缓存中没有，从数据库查询
        admin = adminDao.getUserById(id);
        //若查询出的数据不为null
        if (admin != null) {
            //将数据存入缓存
            MemcachedUtil.setCacheWithNoReply(CachePrefix.USER_MANAGEMENT,String.valueOf(id),admin);
        }
        //返回从数据库查询的admin（当然也可能数据库中也没有，就是null）
        return admin;
    }

    /*********************redis********************/
    public Admin findAdminByIdFromRedis(int id) {
        //从缓存中获取数据
        String adminStr = RedisStringUtil.get(CachePrefix.USER_MANAGEMENT, String.valueOf(id));
        //若缓存中有，直接返回
        if (StringUtils.isNoneBlank(adminStr)) {
            return Admin.parseJsonToAdmin(adminStr);
        }
        //若缓存中没有，从数据查询
        Admin admin = adminDao.getUserById(id);
        //若查询出的数据不为null
        if (admin != null) {
            //将数据存入缓存
            RedisStringUtil.set(CachePrefix.USER_MANAGEMENT,String.valueOf(id),admin.toJson());
        }
        //返回从数据库查询的admin（当然也可能数据库中也么有，就是null）
        return admin;
    }

    /*********************redis hash********************/
    /*
     * 此处用set、list、sorted set都不太好，因为三者都不具备根据key查找值的能力，
     * 以set为例，你缓存的时候，只能缓存一个id进去，最后查询缓存，查到缓存中有ID之后，还需要再根据此ID查询数据库，
     * 才能返回具体的admin，还不如直接根据ID去数据库查询
     *
     * set的一个典型应用场景：
     * 当有用户注册或者用户信息修改或用户被删除之后，我们将其ID放入缓存，
     * 之后可能会启动一个定时任务，定时扫描该set中是否有ID存在，如果有，说明有用户信息发生变化，
     * 然后再进行一些操作，操作之后将set清空。之后继续循环上述的方式
     *
     * 这里有个问题？set的操作是线程安全的吗？
     */
    public Admin findAdminByIdFromRedisHash(int id) {
        //从缓存中获取数据：注意这里可以直接将ResisHashUtil中的hget方法的map改为RedisCacheConstant类型，
        // 下边同理，其他set、list、sorted set也同理
        String adminStr = RedisHashUtil.hget(String.valueOf(RedisCacheConstant.USER_MANAGEMENT_MAP), String.valueOf(id));
        //若缓存中有，直接返回
        if (StringUtils.isNoneBlank(adminStr)) {
            return Admin.parseJsonToAdmin(adminStr);
        }
        //若缓存中没有，从数据库查询
        Admin admin = adminDao.getUserById(id);
        //若查询出的数据不为null
        if (admin != null) {
            //将输入存入缓存
            RedisHashUtil.hset(String.valueOf(RedisCacheConstant.USER_MANAGEMENT_MAP), String.valueOf(id), admin.toJson());
        }
        return admin;
    }

    /**
     * 测试activeMQ
     * <p>
     * 消息生产者做的事：（部署在服务器A）
     * 1）添加一个用户
     * 2）用户添加成功后，
     * 2.1）创建一个Log（日志类）实例
     * 2.2）将该日志实例作为消息发送给消息队列
     * <p>
     * 消息消费者做的事：（部署在服务器B）
     * 1）从队列接收消息
     * 2）用日志处理器对消息进行操作（将该消息写入数据库）
     */
    public boolean register_RPC(Admin admin) {
        boolean isRegisterSuccess = adminDao.register(admin);
        if (isRegisterSuccess) {
            Log log = new Log();
            log.setOperation("增加一个用户");
            log.setCurrentTime(DateUtil.getCurrentTime());

            ActiveMQP2PUtil.sendMessage(log);//将消息发送到消息服务器（即activeMQ服务器），不需要等待消息处理结果，直接向下执行
        }
        return isRegisterSuccess;
    }
}
