package com.microwu.cxd.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   17:50
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/generate/code")
    public String validateCode(HttpServletRequest request) {
        int number = 1000 + (int) (Math.random() * 1000);
        logger.info("======= code = {}======", number);
        request.getSession().setAttribute("validateCode", number);
        return "生成校验码成功";
    }

    @GetMapping("/index")
    public Object index(@AuthenticationPrincipal Principal principal) {
        return principal;
    }

}