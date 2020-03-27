package com.microwu.cxd.service.controller;

import com.microwu.cxd.service.service.SchedualServieHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/10/25   15:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class HiController {
    @Autowired
    private SchedualServieHi schedualServieHi;

    @GetMapping
    public String sayHi(String name){
        return schedualServieHi.sayHiFromClientOne(name);
    }
}