package com.microwu.cxd.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/22   22:33
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class ThreadConfig {
    /**
     * 线程池维护线程的最小数量
     */
    @Value("${thread-pool.core-pool-size:20}")
    private int corePoolSize = 200;
    /**
     * 线程池维护线程的最大数量
     */
    @Value("${thread-pool.max-pool-size:200}")
    private int maxPoolSize = 200;
    /**
     * 缓存队列
     */
    @Value("${thread-pool.queue-capacity:20}")
    private int queueCapacity = 20;
    /**
     * 允许的空闲时间
     */
    @Value("${thread-pool.keep-alive:60}")
    private int keepAlive = 60;
    /**
     * 任务执行处理器
     */
    @Value("${thread-pool.execution-handler:CallerRunsPolicy}")
    private String executionHandler;

    @Bean
    public Executor kdpExecutor() throws Exception {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("kdp-executor-");
        executor.setKeepAliveSeconds(keepAlive);

        // 对线程池队列已满，拒绝task的处理策略
        Class<?> clazz = Class.forName(ThreadPoolExecutor.class.getTypeName() + "$" + executionHandler);
        RejectedExecutionHandler handler = (RejectedExecutionHandler) clazz.getDeclaredConstructor().newInstance();
        executor.setRejectedExecutionHandler(handler);

        executor.initialize();

        return executor;
    }
}