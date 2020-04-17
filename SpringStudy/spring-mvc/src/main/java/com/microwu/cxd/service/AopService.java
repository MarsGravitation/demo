package com.microwu.cxd.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Description: 结论，两个基于AOP的注解不能同时用，会导致一个失效
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/15   10:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class AopService {

    private static final Logger logger = LoggerFactory.getLogger(AopService.class);

    @Async
    @Retryable(value = Exception.class, maxAttempts = 5)
    public void test() {
        logger.info("测试 = {}", "hello world!");
        throw new RuntimeException("Test Exception");
    }

}