package com.microwu.cxd.controller;

import com.microwu.cxd.domain.User;
import com.microwu.cxd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/13   15:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public User index() {
        return userService.getByUsername("cxd");
    }
}