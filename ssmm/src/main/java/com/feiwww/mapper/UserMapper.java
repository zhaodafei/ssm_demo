package com.feiwww.mapper;

import com.feiwww.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {

    @Insert("INSERT INTO userinfo(username, password) VALUES(#{username},#{password})")
    public int insertUser(User user);

    @Select("SELECT * FROM userinfo WHERE username = #{username} AND password = #{password}")
    @Results(value = { @Result(id = true, column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password")})
    public User selectByUsernameAndPwd(@Param("username")String username ,@Param("password")String password);
}
