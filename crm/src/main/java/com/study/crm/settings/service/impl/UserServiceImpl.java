package com.study.crm.settings.service.impl;

import com.study.crm.settings.domain.User;
import com.study.crm.settings.mapper.UserMapper;
import com.study.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByActAndPwd(Map<String, Object> map) {
        return userMapper.selectByActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }
}
