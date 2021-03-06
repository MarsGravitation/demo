package com.microwu.cxd.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/7   15:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String id(@PathVariable Long id) {
        if (1L == id) {
            throw new RuntimeException("不允许它查询用户信息");
        }
        System.out.println("权限验证通过。。。");
        return "i am " + id + " user";
    }
}