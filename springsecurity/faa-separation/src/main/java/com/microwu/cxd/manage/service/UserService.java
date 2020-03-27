package com.microwu.cxd.manage.service;

import com.microwu.cxd.manage.domain.Permission;
import com.microwu.cxd.manage.domain.UserDO;
import com.microwu.cxd.manage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   10:05
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserDO getUsername(String username) {
        return userMapper.getUsername(username);
    }

    public Integer insert(UserDO userDO) {
        return userMapper.insert(userDO);
    }

    public List<Permission> getPermissions(Long id) {
        return userMapper.getPermissions(id);
    }
}