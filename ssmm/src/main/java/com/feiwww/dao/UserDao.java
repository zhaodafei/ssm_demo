package com.feiwww.dao;

import com.feiwww.mapper.UserMapper;
import com.feiwww.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private UserMapper userMapper;

    public boolean register(User user){
        return userMapper.insertUser(user)==1? true:false;
    }

    public User login(String username,String password){
        return userMapper.selectByUsernameAndPwd(username,password);
    }
}
