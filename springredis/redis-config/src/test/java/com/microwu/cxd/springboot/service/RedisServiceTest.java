package com.microwu.cxd.springboot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/14   17:51
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisServiceTest {
    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void test() {
        redisService.set();
    }

    @Test
    public void test02() {
        redisService.transaction();
    }

    @Test
    public void test03() {
        redisService.pipeline();
    }

    @Test
    public void test04() {
        redisService.script();
    }

    @Test
    public void test05() {
        for(int i = 0; i < 10; i++) {
            Boolean login = redisService.frequencyLimitation("login", "18435202728", 60, 1);
            System.out.println(login);
        }
    }

    /**
     * 频率并发测试结果
     *      Thread[cxd-thread-pool-2,5,main]:true
     *      Thread[cxd-thread-pool-1,5,main]:false
     *      Thread[cxd-thread-pool-0,5,main]:true
     *      Thread[cxd-thread-pool-2,5,main]:false
     *
     *      这里应该可以使用循环, 但我不想改了 -_-
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/16  11:38
     *
     * @param
     * @return  void
     */
    @Test
    public void test06() throws ExecutionException, InterruptedException {
        Future<Boolean> login1 = threadPoolExecutor.submit(() -> {
            Boolean login = redisService.frequencyLimitation("login", "18435202728", 60, 1);
            System.out.println(Thread.currentThread() + ":" + login);
            return login;
        });
        Future<Boolean> login2 = threadPoolExecutor.submit(() -> {
            Boolean login = redisService.frequencyLimitation("login", "18435202728", 60, 1);
            System.out.println(Thread.currentThread() + ":" + login);
            return login;
        });
        Future<Boolean> login3 = threadPoolExecutor.submit(() -> {
            Boolean login = redisService.frequencyLimitation("login", "18435202728", 60, 1);
            System.out.println(Thread.currentThread() + ":" + login);
            return login;
        });
        Future<Boolean> login4 = threadPoolExecutor.submit(() -> {
            Boolean login = redisService.frequencyLimitation("login", "18435202728", 60, 1);
            System.out.println(Thread.currentThread() + ":" + login);
            return login;
        });
        System.out.println(login1.get() + "-" + login2.get() + "-" + login3.get() + "-" + login4.get()) ;
    }

    @Test
    public void test07() {
        redisTemplate.opsForValue().set("age", "24");
        String age = redisTemplate.opsForValue().get("age");
        System.out.println(age);
    }

    @Test
    public void test08() throws InterruptedException {
        redisService.structure();
    }
}