package com.microwu.cxd.user.controller;

import com.microwu.cxd.user.domain.User;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/12   16:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/get")
    public User getUser(User user) {
        User userInfo = new User();
        userInfo.setUsername(user.getUsername());
        userInfo.setPassword(user.getPassword());
        return userInfo;
    }

    @PostMapping("/post")
    public User postUser(@RequestBody User user) {
        return  user;
    }

}