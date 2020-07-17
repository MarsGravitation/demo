package com.microwu.cxd.springboot.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.springboot.listener.DefaultMessageDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:     存在的问题，连接池源码？ 如何手动注入连接池？
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/14   17:06
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    private Map<Integer, RedisConnectionFactory> map = new HashMap<>(16);

    /**
     * RedisConnection 提供了Redis 通信的核心构建块，因为它处理与Redis 后端的通信。
     * 它还会自动将基础连接库异常转换为Spring 一致的DAO异常层次结构
     *
     * RedisConnection 是通过RedisConnectionFactory创建的
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/28  16:58
     *
     * @param
     * @return  LettuceConnectionFactory
     */
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("192.168.133.134", 6379);
        // 设置数据库需要使用 redisStandaloneConfiguration
        redisStandaloneConfiguration.setDatabase(15);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    /**
     * 创建指定 db 的 redisConnectionFactory
     * 这里参考圣杰的思路，使用map 做缓存
     * 圣杰是需要传参，通过 配置默认的 db 连接工厂
     * 我这边直接配置吧，不用传参了
     *
     * redisConnectionFactory 是可变对象，每次设置不同的库，需要生成一个新的对象
     * 否则就会改变以前的设置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/29  9:48
     *
     * @param
     * @return  org.springframework.data.redis.connection.RedisConnectionFactory
     */
    private RedisConnectionFactory getConnectionFactory(int database) {
        if (map.containsKey(database)) {
            // 存在就直接返回
            return map.get(database);
        }
        // 这里圣杰又分为了 哨兵，集群，单机版
        // 我这里使用配置进行判断，圣杰是使用 自动注入的默认工厂进行配置
        // 我不确定我的是否正确
        if (redisProperties.getSentinel() != null) {
            System.out.println("哨兵模式 启动。。。");
            return null;
        } else if (redisProperties.getCluster() != null) {
            System.out.println("集群模式 启动。。。");
            return null;
        } else {
            System.out.println("单机模式 启动。。。");
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
            // 设置数据库需要使用 redisStandaloneConfiguration
            redisStandaloneConfiguration.setDatabase(database);
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
            map.putIfAbsent(database, lettuceConnectionFactory);

            // 这里需要进行after，这里会进行一些默认配置 -_- 坑啊
            // 为什么上面的类不需要进行设置，我感觉它加了@Bean注解，spring会自动调用它的afterProperties
            ((LettuceConnectionFactory) map.get(database)).afterPropertiesSet();
            return map.get(database);
        }
    }

    /**
     * 该模版是线程安全的，并且可以在多个实例之间重用
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/28  17:02
     *
     * @param
     * @return  org.springframework.data.redis.core.RedisTemplate
     */
    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key 使用 String序列化对象 - 但是序列化long类型会报错
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value 使用 jackson序列化对象 - 序列化字符串对象会带双引号
        redisTemplate.setValueSerializer(jackson2JsonRedisSerialize());

//        LettuceConnectionFactory lettuceConnectionFactory = lettuceConnectionFactory();
        // 这里面设置是无效的, 虽然底层也是通过 redisStandaloneConfiguration 设置数据库, 但是不知道为什么不行
//        lettuceConnectionFactory.setDatabase(15);
        redisTemplate.setConnectionFactory(getConnectionFactory(15));
        // 这个是进行一些默认配置
        // 这里加不加都无所谓，spring会自动调用这个方法
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

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new DefaultMessageDelegate());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(getConnectionFactory(14));
//        Map<? extends MessageListener, Collection<? extends Topic>> listeners = new HashMap<>(1);
//        ArrayList<Topic> topics = new ArrayList<>(1);
//        topics.add(new ChannelTopic(""));
//        listeners.put(messageListenerAdapter(), topics);
//        redisMessageListenerContainer.setMessageListeners(listeners);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter(), new PatternTopic("__keyevent@14__:expired"));
        return redisMessageListenerContainer;
    }
}