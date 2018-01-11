package com.feiwww.service;

import com.feiwww.dao.UserDao;
import com.feiwww.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public boolean register(User user){
        return userDao.register(user);
    }

    public User login(String username,String password){
        return userDao.login(username,password);
    }
}
