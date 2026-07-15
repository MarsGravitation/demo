package com.microwu.cxd.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/21   9:50
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${common.name}")
    private String name;

    @GetMapping("/name")
    public String name() {
        return name;
    }

}
