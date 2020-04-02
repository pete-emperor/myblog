package com.pyt.dao;

import com.pyt.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by PC on 2020/4/1.
 */
@Mapper
@Component
public interface UserDao {

   public  UserInfo getUserInfo(UserInfo userInfo);

}
