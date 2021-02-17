package com.microwu.cxd.study.limit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/30   11:25
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LimitTest {

    public static void main(String[] args) {
        // 每秒生成 1 个令牌
        RateLimiter rateLimiter = RateLimiter.create(1);
        for (int i = 1; i < 5; i++) {
            // 阻塞式获取令牌
            // 返回等待时间，如果是 1 的话，就是平滑效果
            // 如果处理突发流量的话，可以传 n
            // tryAcquire 设置超时时间获取令牌，timeout = 0 表示非阻塞，获取不到立即返回
            double acquire = rateLimiter.acquire(i);
            // 支持预消费
            System.out.println("curTime = " + System.currentTimeMillis() + " acq: " + i + " waitTime: " + acquire);
        }
    }

}