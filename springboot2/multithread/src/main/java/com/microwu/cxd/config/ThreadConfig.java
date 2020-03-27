package com.microwu.cxd.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/8   11:01
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@EnableAsync
public class ThreadConfig {
    @Bean
    public Executor executors() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cxd-%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20,
                30, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolExecutor;
    }
}