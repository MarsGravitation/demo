package com.microwu.cxd.chinese.hello;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/21   15:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RedisClientTest {

    public void test() {
        // 连接 Redis 服务器
        // 配置参数包装在 RedisOption  对象中
        RedisOptions config = new RedisOptions().setHost("192.168.133.134");

        RedisClient redisClient = RedisClient.create(Vertx.vertx(), config);

        // 执行命令
        redisClient.get("myKey", res -> {
            if (res.succeeded()) {
                // do something
            }
        });
    }

}