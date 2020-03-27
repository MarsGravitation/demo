package com.microwu.cxd.service;

import com.microwu.cxd.common.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/22   10:18
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisServiceTest {
    @Autowired
    private RedisServer redisServer;

    @Test
    public void test() {
        redisServer.setKey();
    }

    @Test
    public void test01() {
        for (int i = 0; i < 10; i++) {
            boolean result = RedisUtils.requestFrequencyLimit("name", 60, 1);
            System.out.println("============= " + result);
        }
    }
}