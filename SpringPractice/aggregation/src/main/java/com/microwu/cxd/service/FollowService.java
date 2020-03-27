package com.microwu.cxd.service;

import com.microwu.cxd.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/4   9:51
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class FollowService {
    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);

    public List<User> listById(Long id) {
        logger.info("=======粉丝列表=======");
        ArrayList<User> users = new ArrayList<User>(5);
        for(int i = 0; i < users.size(); i++ ) {
            User user = new User();
            user.setId((long)i);
            users.add(user);
        }
        return users;
    }
}