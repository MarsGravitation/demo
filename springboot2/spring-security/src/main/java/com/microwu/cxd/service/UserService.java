package com.microwu.cxd.service;

import com.microwu.cxd.domain.Permission;
import com.microwu.cxd.domain.User;
import com.microwu.cxd.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/13   15:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    public List<Permission> getPermissions(Long id) {
        return userMapper.getPermissions(id);
    }

    public User getByMobile(String mobile) {
        return userMapper.getByMobile(mobile);
    }
}