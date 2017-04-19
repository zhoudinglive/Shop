package com.shop.Service;

import com.shop.Dao.UserDao;
import com.shop.Pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Carpenter on 2017/3/7.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public boolean insert(User user){
        try{
            return userDao.insert(user);
        }catch (Exception e){
            return false;
        }
    }

    public boolean delete(String username){
        try{
            return userDao.delete(username);
        }catch (Exception e){
            return false;
        }
    }

    public User select(String username){
        try {
            return userDao.getUserByUsername(username);
        }catch (Exception e){
            return null;
        }
    }

    public boolean updatePassword(String username, String password){
        try{
            return userDao.updatePasswordByUsername(username, password);
        }catch (Exception e){
            return false;
        }
    }

    public boolean updateRole(String username, String role){
        try {
            return userDao.updateRoleByUsername(username, role);
        }catch (Exception e){
            return false;
        }
    }
}
