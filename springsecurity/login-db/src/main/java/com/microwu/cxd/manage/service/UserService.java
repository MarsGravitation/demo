package com.microwu.cxd.manage.service;

import com.microwu.cxd.manage.domain.UserDAO;
import com.microwu.cxd.manage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/16   14:41
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserDAO getByUsername(String username) {
        UserDAO userDAO = userMapper.getByUsername(username);
        return userDAO;
    }

    public Integer insertUser(UserDAO userDAO) {
        userDAO.setPassword(new BCryptPasswordEncoder().encode(userDAO.getPassword()));
        return userMapper.insertUser(userDAO);
    }
}