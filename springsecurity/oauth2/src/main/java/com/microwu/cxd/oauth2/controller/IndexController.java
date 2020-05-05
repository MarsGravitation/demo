package com.microwu.cxd.oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/5/1   14:55
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */
@RestController
public class IndexController {

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable String id) {
        //for debug
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "product id : " + id;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order/{id}")
    public String getOrder(@PathVariable String id) {
        //for debug
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "order id : " + id;
    }

}