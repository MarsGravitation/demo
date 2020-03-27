package com.microwu.cxd.manage.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/23   17:58
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class IndexController {
    @GetMapping("/index")
    public Object index(@AuthenticationPrincipal Principal principal) {
        return principal;
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal Principal principal) {
        return principal.getName();
    }
}