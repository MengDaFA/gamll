package com.atguigu.gmall1205.service;

import com.atguigu.gmall1205.bean.UserAddress;
import com.atguigu.gmall1205.bean.UserInfo;

import java.util.List;

public interface UserService {

    /**
     * 查询所有用户信息
     * @return
     */
    List<UserInfo> findAll();

    /**
     *  根据用户Id 查询用户地址列表
     * @param userId
     * @return
     */
    List<UserAddress> findAddressByUserId(String userId);
}
