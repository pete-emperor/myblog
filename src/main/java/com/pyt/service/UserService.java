package com.pyt.service;

import com.pyt.bean.UserInfo;
import com.pyt.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by PC on 2020/4/1.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserInfo getUserInfo(UserInfo userInfo){
        return userDao.getUserInfo(userInfo);
    }
}
