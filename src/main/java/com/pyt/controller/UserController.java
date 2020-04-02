package com.pyt.controller;

import com.pyt.bean.UserInfo;
import com.pyt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by PC on 2020/4/1.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUser")
    public UserInfo getUser(){
        UserInfo userInfo = new UserInfo();
        userInfo.setName("小明");
        userInfo.setPassword("123");
        userInfo = userService.getUserInfo(userInfo);
        return userInfo;
    }

}
