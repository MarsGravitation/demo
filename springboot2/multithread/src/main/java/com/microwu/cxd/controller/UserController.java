package com.microwu.cxd.controller;

import com.microwu.cxd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/8   11:29
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register() throws InterruptedException {
        userService.sendMsg();
        userService.sendMail();
        return "hello";
    }

    @RequestMapping("test02")
    public String test02() throws InterruptedException {
        userService.send();
        return "test02";
    }
}