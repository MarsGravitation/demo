package com.microwu.cxd.docker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/20   10:10
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class HelloController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/add")
    public String add() {
        Long money = redisTemplate.opsForValue().increment("money");
        return "money: " + money;
    }

}