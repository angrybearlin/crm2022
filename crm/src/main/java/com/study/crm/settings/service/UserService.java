package com.study.crm.settings.service;

import com.study.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByActAndPwd(Map<String, Object> map);

    List<User> queryAllUsers();
}
