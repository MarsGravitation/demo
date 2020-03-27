package com.microwu.cxd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/22   10:16
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class RedisServer {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setKey() {
        redisTemplate.opsForValue().set("name", "cxd");
    }
}