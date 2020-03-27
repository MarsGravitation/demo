package com.microwu.cxd.springboot.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/16   11:13
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
    @Bean
    public ThreadPoolExecutor threadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cxd-thread-pool-%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10),
                threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolExecutor;
    }
}