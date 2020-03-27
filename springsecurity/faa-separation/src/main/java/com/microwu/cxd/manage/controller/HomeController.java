package com.microwu.cxd.manage.controller;

import com.microwu.cxd.manage.bean.WebResponse;
import com.microwu.cxd.manage.domain.UserDO;
import com.microwu.cxd.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   9:48
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class HomeController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public WebResponse doRegister(@RequestBody UserDO userDO) {
        userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
        userService.insert(userDO);
        return WebResponse.success("注册成功");
    }

    @GetMapping("/index")
    public WebResponse index(@AuthenticationPrincipal Principal principal) {
        return WebResponse.success("欢迎" + principal + "回来");
    }

}