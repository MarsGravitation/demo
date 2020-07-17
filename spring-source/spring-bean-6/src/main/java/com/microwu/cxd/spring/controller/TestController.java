package com.microwu.cxd.spring.controller;

import com.microwu.cxd.spring.service.TestService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   14:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@Controller
public class TestController {
    @Autowired
    private TestService testService;
}