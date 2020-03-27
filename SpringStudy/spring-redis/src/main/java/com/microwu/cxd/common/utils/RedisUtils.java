package com.microwu.cxd.common.utils;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/23   9:57
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RedisUtils {

    private static RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);

    public static boolean requestFrequencyLimit(String key, int period, int count) {
        List list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                // 获取当前时间戳
                long timeMillis = System.currentTimeMillis();
                byte[] bytes = key.getBytes();
                // 开启事务
                redisConnection.multi();
                // 添加到有序集合中
                redisConnection.zAdd(bytes, timeMillis, String.valueOf(timeMillis).getBytes());
                // 移除score在min和max之间的key
                redisConnection.zRemRangeByScore(bytes, 0, timeMillis - period * 1000);
                // 获取有序集合的大小
                redisConnection.zCard(bytes);
                // 设置过期时间
                redisConnection.expire(bytes, period + 1);
                // 提交事务
                redisConnection.exec();
                redisConnection.close();

                return null;
            }
        });
        // 获取有序集合大小 --- 结果[true, 0, 2, true], [], []
        // 结果为一个集合, 元素也是一个集合, 元素中是没条命令返回的结果
        long l = (long)((List) list.get(0)).get(2);
        System.out.println("================ " + l);
        return l <= count;
    }
}