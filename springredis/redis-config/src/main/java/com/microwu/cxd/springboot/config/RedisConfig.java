package com.microwu.cxd.springboot.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/14   17:06
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("192.168.133.134", 6379);
        // 设置数据库需要使用 redisStandaloneConfiguration
        redisStandaloneConfiguration.setDatabase(15);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key 使用 String序列化对象 - 但是序列化long类型会报错
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value 使用 jackson序列化对象 - 序列化字符串对象会带双引号
        redisTemplate.setValueSerializer(jackson2JsonRedisSerialize());

        LettuceConnectionFactory lettuceConnectionFactory = lettuceConnectionFactory();
        // 这里面设置是无效的, 虽然底层也是通过 redisStandaloneConfiguration 设置数据库, 但是不知道为什么不行
        lettuceConnectionFactory.setDatabase(15);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        // 这个和spring相关, 加不加好像并没有什么关系
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * json 序列化
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/15  11:24
     *
     * @param
     * @return  org.springframework.data.redis.serializer.RedisSerializer<java.lang.Object>
     */
    @Bean
    public RedisSerializer<Object> jackson2JsonRedisSerialize() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;

    }
}