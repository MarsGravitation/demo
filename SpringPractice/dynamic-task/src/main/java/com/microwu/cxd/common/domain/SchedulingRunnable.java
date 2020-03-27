package com.microwu.cxd.common.domain;

import com.microwu.cxd.common.utils.SpringUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalTime;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/9   21:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SchedulingRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingRunnable.class);

    private String beanName;

    private String methodName;

    private String methodParams;

    public SchedulingRunnable(String beanName, String methodName) {
        this(beanName, methodName, null);
    }

    public SchedulingRunnable(String beanName, String methodName, String methodParams) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }

    @SneakyThrows
    @Override
    public void run() {
        logger.info("定时任务开始执行, bean = {}, method = {}, params = {}, start_time = {}",
                beanName, methodName, methodParams, LocalTime.now());
        Object target = SpringUtils.getBean(beanName);
        Class<?> clazz = target.getClass();
        Method method;
        if(!StringUtils.isEmpty(methodParams)) {
            method = clazz.getDeclaredMethod(methodName, String.class);
        } else {
            method = clazz.getDeclaredMethod(methodName);
        }

        ReflectionUtils.makeAccessible(method);
        if(!StringUtils.isEmpty(methodParams)) {
            method.invoke(target, methodParams);
        } else {
            method.invoke(target);
        }

        logger.info("定时任务开始结束, end_time = {}", LocalTime.now());
    }
}