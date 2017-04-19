package com.shop.Dao;

import com.shop.Pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by Carpenter on 2017/3/7.
 */
@Mapper
public interface UserDao {
    @Insert("INSERT INTO user (username, password, role) VALUES (#{username}, #{password}, #{role});")
    boolean insert(User user);

    @Delete("DELETE FROM user WHERE username=#{username};")
    boolean delete(String username);

    @Select("SELECT * FROM user WHERE username=#{username};")
    User getUserByUsername(String username);

    @Update("UPDATE user SET password=#{password} where username = #{username};")
    boolean updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    @Update("UPDATE user SET role=#{role} where username = #{username};")
    boolean updateRoleByUsername(@Param("username") String username, @Param("role") String role);
}
