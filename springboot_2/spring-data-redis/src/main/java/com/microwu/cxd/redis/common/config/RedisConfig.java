package com.microwu.cxd.redis.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Description:     Lettuce连接
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/3   14:38
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class RedisConfig {
    /**
     * RedisConnection为redis通信提供核心构建块,因为它处理与redis后端的通信.
     *
     * 默认情况下,LettuceConnection创建的所有的LettuceConnection实例对于所有非阻塞和非事务性操作
     * 都共享相同的线程安全本机连接.
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/6/3  14:41
     *
     * @param   	
     * @return  org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("192.168.133.134", 6379));
    }
}