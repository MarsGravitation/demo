package com.microwu.cxd.service;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/16   9:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class GuavaRetryService {

    private static final Logger logger = LoggerFactory.getLogger(GuavaRetryService.class);

    /**
     * guava 的重试分为两步：
     *  1. 使用工厂模式构造重试器
     *  2. 执行重试方法并得到结果
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/16  9:45
     *
     * @param
     * @return  java.lang.String
     */
    @Async
    public String retry() {
        // 1. 定义重试机制
        Retryer<String> retry = RetryerBuilder.<String>newBuilder()
                // retryIf 重试条件
//                .retryIfException()
//                .retryIfRuntimeException()
                // 返回true 表示需要重试
                .retryIfResult(Predicates.or(Predicates.equalTo("fail"), Predicates.equalTo("null")))

                // 等待策略：每次请求间隔1秒
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))

                // 停止策略： 尝试请求5次
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))

                // 时间限制：某次请求不得超过2s， - 这个方法有问题
                // 可能和guava 的版本有关，高版本 SimpleTimeLimiter 是private，不能new()
//                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))

                .build();

        String call = null;
        try {
            call = retry.call(new Callable<String>() {
                int times = 0;

                @Override
                public String call() throws Exception {
                    logger.info("call times = {}", times);
                    times++;

                    if (times == 1) {
                        throw new Exception();
                    } else if (times == 2) {
                        throw new NullPointerException();
                    } else if (times == 3) {
                        throw new Exception();
                    } else if (times == 4) {
                        return "null";
                    } else if (times == 5) {
                        return "fail";
                    } else {
                        return "success";
                    }
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            e.printStackTrace();
        }

        logger.info("重试结果： {}", call);
        return call;
    }

}