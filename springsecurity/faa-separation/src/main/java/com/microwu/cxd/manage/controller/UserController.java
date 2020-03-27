package com.microwu.cxd.manage.controller;

import com.microwu.cxd.manage.bean.WebResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   9:51
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class UserController {

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('cxd:role:query')")
    public WebResponse user(@AuthenticationPrincipal Principal principal) {
        return WebResponse.success(principal.getName());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('cxd:order:query')")
    public WebResponse admin(@AuthenticationPrincipal Principal principal) {
        return WebResponse.success(principal.getName());
    }

}