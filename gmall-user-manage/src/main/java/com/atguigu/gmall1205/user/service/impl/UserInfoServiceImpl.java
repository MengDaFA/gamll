package com.atguigu.gmall1205.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall1205.bean.UserAddress;
import com.atguigu.gmall1205.bean.UserInfo;
import com.atguigu.gmall1205.service.UserService;
import com.atguigu.gmall1205.user.mapper.UserAddressMapper;
import com.atguigu.gmall1205.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserAddress> findAddressByUserId(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }
}
