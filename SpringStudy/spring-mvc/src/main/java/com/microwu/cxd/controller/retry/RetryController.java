package com.microwu.cxd.controller.retry;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/21   14:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class RetryController {

    @GetMapping("/retry")
    public String retry() {
        http(1);
        return "success";
    }

    @GetMapping("/spring/retry")
    public String sprintRetry() {
        // 构建重试模板实例
        RetryTemplate retryTemplate = new RetryTemplate();
        // 设置重试策略, 主要设置重试次数和需要捕获的异常
        SimpleRetryPolicy policy = new SimpleRetryPolicy(5, Collections.singletonMap(Exception.class, true));
        // 设置重试回退操作策略, 主要设置重试间隔时间
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        // 初始间隔
        backOffPolicy.setInitialInterval(1000);
        // 最大间隔
        backOffPolicy.setMaxInterval(10 * 1000L);
        // 递增倍数
        backOffPolicy.setMultiplier(2);

        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        // 通过RetryCallBack 重试回调实例包装正常逻辑, 第一次执行和重试执行都是这段逻辑

        final RetryCallback<Object, Exception> retryCallback = context -> {
            System.out.println("重试开始: " + context.getRetryCount());
            springHttp();
            return null;
        };

        // 通过RecoveryCallback 重试流程正常结束或者达到重试上线后的退出恢复操作实例
        final RecoveryCallback<Object> recoveryCallback = context -> {
            System.out.println("执行重试结束失败后的代码");
            return null;
        };
        try{
            // 由RetryTemplate 执行execute 方法开始逻辑执行
            retryTemplate.execute(retryCallback, recoveryCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }

    /**
     * AOP失效:
     *  1. AOP 不能使用private
     *  2. 本类的其他方法调用AOP方法会失效, 因为调用的本类对象, 而不是代理对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/1/21  16:45
     *
     * @param
     * @return  java.lang.String
     */
    @GetMapping("/annotation/retry")
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public String annotationRetry() {
        springHttp();
        return "success";
    }

    /**
     * 使用递归进行重试
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/1/21  15:31
     *
     * @param   	count
     * @return  void
     */
    private void http(int count) {
        try {
            System.out.println("第 " + count + "次 发送http请求... ");
            if(count < 5) {
                throw new RuntimeException("请求超时, 请重试 ...");
            }
            System.out.println("发送成功");
        } catch (Exception e) {

            http(++count);
        }
    }

//    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    private void springHttp() {
        System.out.println("发送http请求... ");
        throw new RuntimeException("请求超时, 请重试....");
    }

    /**
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/1/21  16:21
     *
     * @param   	e
     * @return  void
     */
    @Recover
    private void recover(Exception e) {
        e.printStackTrace();
        System.out.println("重试结束或者重试达到次数, 执行该方法...");
    }
}