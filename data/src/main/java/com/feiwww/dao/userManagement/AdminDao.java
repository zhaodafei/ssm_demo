package com.feiwww.dao.userManagement;

import com.feiwww.mapper.userManagement.AdminMapper;
import com.feiwww.model.userManagement.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理员DAO
 */
@Repository
public class AdminDao {
    @Autowired
    private AdminMapper adminMapper; //TODO(这里为啥一直是红色的2）

    public boolean register(Admin admin){
        return adminMapper.insertAdmin(admin)==1? true:false;
    }

    public Admin login(String username , String password){
        return adminMapper.selectAdmin(username, password);
    }

    public List<Admin> findAdmin(String username, String password, int start, int limit){
        return adminMapper.getAdminByConditions(username, password, start, limit);
    }

    public int insertAdminWithBackId(Admin admin){
        return adminMapper.insertAdminWithBackId(admin);
    }

    /******************guava cache********************/
    public List<Admin> getUserByName(String username){
        return adminMapper.getUserByName(username);
    }

    /******************memcached********************/
    public Admin getUserById(int id){
        return adminMapper.selectById(id);
    }
}
