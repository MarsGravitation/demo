package com.microwu.cxd.service;

import com.microwu.cxd.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/4   9:44
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public User getById(Long id) {
        logger.info("======用户信息=====");
        User user = new User();
        user.setUserName("cxd");
        user.setPassword("123456");
        return user;
    }

}