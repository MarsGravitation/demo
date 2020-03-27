package com.microwu.cxd.controller;

import com.microwu.cxd.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   11:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('cxd:order:query')")
    public User index(@AuthenticationPrincipal User user) {
        return user;
    }
}