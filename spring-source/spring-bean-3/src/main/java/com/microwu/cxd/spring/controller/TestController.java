package com.microwu.cxd.spring.controller;

import com.microwu.cxd.spring.service.TestService;
import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   17:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class TestController {

    TestService testService;

    String name;

    public TestController(TestService testService, String name) {
        this.testService = testService;
        this.name = name;
    }

}