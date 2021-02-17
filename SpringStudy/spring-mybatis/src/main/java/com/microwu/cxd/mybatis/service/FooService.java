package com.microwu.cxd.mybatis.service;

import com.microwu.cxd.mybatis.mapper.UserMapper;
import com.microwu.cxd.mybatis.pojo.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/21   16:24
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Transactional
public class FooService {

    private final UserMapper userMapper;

    public FooService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User doSomeBusinessStuff(String userId) {
        return this.userMapper.getUser(userId);
    }

}