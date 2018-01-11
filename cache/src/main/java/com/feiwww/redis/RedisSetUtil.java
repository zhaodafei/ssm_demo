package com.feiwww.redis;

import redis.clients.jedis.ShardedJedis;

import java.util.Set;

public class RedisSetUtil extends RedisBaseUtil {
    /***************************添加缓存*****************************/
    /**
     * 添加缓存
     *
     * @param set    缓存将要添加到的set集合
     * @param values 添加的缓存元素
     */
    public static void sadd(String set, String... values) {
        boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
        ShardedJedis jedis = null;
        try {
            jedis = getJedis();//获取jedis实例
            if (jedis == null) {
                broken = true;
                return;
            }
            /*
             * 对比：
             * lpush(String key, String... strings);
             * 返回push之后的list中包含的元素个数
             *
             * sadd(String key, String... members)
             * 1：添加元素成功
             * 0：set中已经有要添加的元素了
             */
            jedis.sadd(set, values);
        } catch (Exception e) {
            broken = true;
        } finally {
            returnJedis(jedis, broken);
        }
    }

    /***************************获取缓存*****************************/
    /**
     * 获取set集合中的所有缓存
     *
     * @param set
     */
    public static Set<String> smembers(String set) {
        boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
        ShardedJedis jedis = null;
        try {
            jedis = getJedis();//获取jedis实例
            if (jedis == null) {
                broken = true;
                return null;
            }
            return jedis.smembers(set);
        } catch (Exception e) {
            broken = true;
        } finally {
            returnJedis(jedis, broken);
        }
        return null;
    }

    /***************************删除缓存*****************************/
    /**
     * 删除缓存
     *
     * @param set
     * @param values
     */
    public static void sremove(String set, String... values) {
        boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
        ShardedJedis jedis = null;
        try {
            jedis = getJedis();//获取jedis实例
            if (jedis == null) {
                broken = true;
                return;
            }
            jedis.srem(set, values);
        } catch (Exception e) {
            broken = true;
        } finally {
            returnJedis(jedis, broken);
        }
    }

    /***************************其他*****************************/
    /**
     * set集合是否包含value
     *
     * @param set
     */
    public static boolean sismembers(String set, String value) {
        boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
        ShardedJedis jedis = null;
        try {
            jedis = getJedis();//获取jedis实例
            if (jedis == null) {
                broken = true;
                return false;
            }
            return jedis.sismember(set, value);
        } catch (Exception e) {
            broken = true;
        } finally {
            returnJedis(jedis, broken);
        }
        return false;
    }

    /**
     * 返回set集合的元素个数
     *
     * @param set
     */
    public static long ssize(String set) {
        boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
        ShardedJedis jedis = null;
        try {
            jedis = getJedis();//获取jedis实例
            if (jedis == null) {
                broken = true;
                return 0;
            }
            return jedis.scard(set);
        } catch (Exception e) {
            broken = true;
        } finally {
            returnJedis(jedis, broken);
        }
        return 0;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        sadd("adminset", "nana", "jigang");
        sadd("adminset", "nana2");
        System.out.println(smembers("adminset"));
        System.out.println(ssize("adminset"));
        System.out.println(sismembers("adminset", "jigang"));
        sremove("adminset", "jigang");
        System.out.println(sismembers("adminset", "jigang"));
    }

}
