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
 * Date:       2020/1/14   11:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('cxd:role:query')")
    public User index(@AuthenticationPrincipal User user) {
        return user;
    }
}