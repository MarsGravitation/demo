package com.microwu.cxd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/1   13:50
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class HelloController {

    @GetMapping("/index")
    public String index(HttpSession session) {
        session.setAttribute("name", "cxd");
        return "index";
    }
}