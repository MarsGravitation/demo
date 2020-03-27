package com.microwu.cxd.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.common.utils.CommonUtils;
import com.microwu.cxd.common.utils.enums.RedisKeyGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Redis配置类
 *
 * @author      Sage Ro             shengjie.luo@microwu.com
 * @date        2018/5/30  17:09
 * CopyRight    北京小悟科技有限公司    http://www.microwu.com
 *
 * Update History:
 *   Author        Time            Content
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * multi databases RedisConnectionFactory
     */
    private Map<Integer, RedisConnectionFactory> factories = new HashMap<>(16, 1);


    /**
     * 注解@Cache的Key生成器
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/5/30  18:48
     *
     * @return KeyGenerator
     */
    @Bean
    @Primary
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 没有定义key值的注解@Cache的Key生成器
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/8/13  14:23
     *
     * @return KeyGenerator
     */
    @Bean
    public KeyGenerator nonDefKeyGenerator() {
        return (target, method, params) -> {
            Cacheable cacheable = method.getDeclaredAnnotation(Cacheable.class);
            String cacheName = cacheable.cacheNames()[0];

            return CommonUtils.getLastCamelWord(cacheName);
        };
    }

    /**
     * 注解@Cache的默认缓存管理器
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/5/30  18:48
     *
     * @param factory RedisConnectionFactory
     * @return CacheManager
     */
    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.builder(factory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        // set value serializer
                        .serializeValuesWith(RedisSerializationContext
                                .SerializationPair.fromSerializer(jacksonRedisSerializer()))
                        .computePrefixWith(cacheName -> RedisKeyGenerator.CXD.generateKey(cacheName) + ":"))
                .build();
    }

    /**
     * 注解@Cache的缓存管理器(10分钟失效)
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/7/10  16:00
     *
     * @param factory RedisConnectionFactory
     * @return CacheManager
     */
    @Bean
    public CacheManager expiredCacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.builder(factory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        // set value serializer
                        .serializeValuesWith(RedisSerializationContext
                                .SerializationPair.fromSerializer(jacksonRedisSerializer()))
                        .computePrefixWith(cacheName -> RedisKeyGenerator.CXD.generateKey(cacheName) + ":")
                        // 设置缓存过期时间
                        .entryTtl(Duration.ofMinutes(10)))
                .build();
    }

    /**
     * redis template
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/5/30  18:49
     *
     * @param factory RedisConnectionFactory
     * @return RedisTemplate
     */
    @Bean
    @Primary
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);
        // 设置序列化器
        setTemplateSerializer(template);

        template.afterPropertiesSet();

        return template;
    }

    /**
     * string redis template
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/5/30  18:49
     *
     * @param factory RedisConnectionFactory
     * @return StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);

        template.setConnectionFactory(factory);
        // 设置序列化器
        setTemplateSerializer(template);

        template.afterPropertiesSet();

        return template;
    }

    /**
     * 创建指定db的RedisConnectionFactory
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018-11-30  16:18
     *
     * @param factory LettuceConnectionFactory
     * @param database   db number
     * @return RedisConnectionFactory
     */
    private RedisConnectionFactory getConnectionFactory(LettuceConnectionFactory factory, int database) {
        if (!factories.containsKey(database)) {
            if (database == 0) {
                factories.putIfAbsent(0, factory);
                return factory;
            }
            if (factory.getSentinelConfiguration() != null) {
                RedisSentinelConfiguration sentinelConfiguration = factory.getSentinelConfiguration();
                RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                config.master(Objects.requireNonNull(sentinelConfiguration.getMaster()));
                config.setSentinels(sentinelConfiguration.getSentinels());
                config.setPassword(sentinelConfiguration.getPassword());
                config.setDatabase(database);

                factories.putIfAbsent(database, new LettuceConnectionFactory(config, factory.getClientConfiguration()));
            } else if (factory.getClusterConfiguration() != null) {
                RedisClusterConfiguration clusterConfiguration = factory.getClusterConfiguration();
                RedisClusterConfiguration config = new RedisClusterConfiguration();
                config.setClusterNodes(clusterConfiguration.getClusterNodes());
                if (clusterConfiguration.getMaxRedirects() != null) {
                    config.setMaxRedirects(clusterConfiguration.getMaxRedirects());
                }
                config.setPassword(clusterConfiguration.getPassword());

                factories.putIfAbsent(database, new LettuceConnectionFactory(config, factory.getClientConfiguration()));
            } else {
                RedisStandaloneConfiguration standaloneConfiguration = factory.getStandaloneConfiguration();
                RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
                config.setHostName(standaloneConfiguration.getHostName());
                config.setPort(standaloneConfiguration.getPort());
                config.setPassword(standaloneConfiguration.getPassword());
                config.setDatabase(database);

                factories.putIfAbsent(database, new LettuceConnectionFactory(config, factory.getClientConfiguration()));
            }
            ((LettuceConnectionFactory) factories.get(database)).afterPropertiesSet();
            ((LettuceConnectionFactory) factories.get(database)).setDatabase(database);
        }
        return factories.get(database);
    }

    /**
     * 设置模板序列化器
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/6/12  10:19
     *
     * @param template
     * @param <T>
     * @param <U>
     */
    private <T, U> void setTemplateSerializer(RedisTemplate<T, U> template) {
        // key序列化方式，但是如果方法上有Long等非String类型的话，会报类型转换错误
        RedisSerializer<String> redisKeySerializer = new StringRedisSerializer();

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(redisKeySerializer);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        template.setValueSerializer(jacksonRedisSerializer());
    }

    /**
     * Jackson序列化器
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/8/13  13:10
     *
     * @return Jackson2JsonRedisSerializer
     */
    private Jackson2JsonRedisSerializer<?> jacksonRedisSerializer() {
        Jackson2JsonRedisSerializer redisValueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        redisValueSerializer.setObjectMapper(objectMapper);

        return redisValueSerializer;
    }
}
