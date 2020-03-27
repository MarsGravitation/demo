package com.microwu.cxd.controller;

import com.microwu.cxd.annotation.MyController;
import com.microwu.cxd.annotation.MyRequestMapping;
import com.microwu.cxd.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/24   9:52
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@MyController
@MyRequestMapping("/test")
public class TestController {

    @MyRequestMapping("doTest")
    public void test(HttpServletRequest request, HttpServletResponse response, @MyRequestParam("param") String param) {
        System.out.println(param);
        try {
            response.getWriter().write("doTest method success ! param :" + param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}