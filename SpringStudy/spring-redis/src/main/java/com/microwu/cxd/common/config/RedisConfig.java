package com.microwu.cxd.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.common.listener.DefaultMessageDelegate;
import com.microwu.cxd.listener.TopicMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.*;

/**
 * Description:     @EnableRedisHttpSession注释创建一个SpringBean, 名称为SpringSessionRepositoryFilter
 *                  实现过滤器. 过滤器负责替换由Spring会话支持的HttpSession实现. 在这种情况下, Spring
 *                  会话由Redis支持. 我们创建了一个RedisConnectionFactory, 它将Spring会话连接到Redis服务器.
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/22   10:10
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableRedisHttpSession
public class RedisConfig {
    @Autowired
    private TopicMessageListener topicMessageListener;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("192.168.133.134", 6379));
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        setRedisSerializer(redisTemplate);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    @Bean
//    public RedisMessageListenerContainer messageListenerContainer() {
//        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
//        redisMessageListenerContainer.setConnectionFactory(lettuceConnectionFactory());
//        redisMessageListenerContainer.addMessageListener(topicMessageListener, new PatternTopic("__keyevent@0__:expired"));
//        return  redisMessageListenerContainer;
//    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new DefaultMessageDelegate());
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(lettuceConnectionFactory());
        Map<MessageListener, Collection<? extends Topic>> listeners = new HashMap<>(1);
        List<Topic> topics = new ArrayList<>(1);
        topics.add(new PatternTopic("__keyevent@14__:expired"));
        listeners.put(messageListenerAdapter(), topics);
        redisMessageListenerContainer.setMessageListeners(listeners);
//        redisMessageListenerContainer.addMessageListener(messageListenerAdapter(), new PatternTopic("__keyevent@14__:expired"));
        return  redisMessageListenerContainer;
    }

    private <K, V> void setRedisSerializer(RedisTemplate<K, V> redisTemplate) {
        // key 序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

        // value 序列化方式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    }
}