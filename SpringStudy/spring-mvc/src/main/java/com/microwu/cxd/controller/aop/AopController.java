package com.microwu.cxd.controller.aop;

import com.microwu.cxd.service.AopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 为了测试重试和异步，也就是 多个底层使用AOP的注解用于一个方法
 *      会发生冲突吗
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/15   10:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/aop")
public class AopController {

    @Autowired
    private AopService aopService;

    @RequestMapping("/test")
    public String test() {
        aopService.test();
        return "success";
    }

}