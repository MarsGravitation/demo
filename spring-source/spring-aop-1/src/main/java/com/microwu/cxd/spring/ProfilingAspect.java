package com.microwu.cxd.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/3   9:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Aspect
public class ProfilingAspect {

    @Around("methodsToProfild()")
    public Object profile(ProceedingJoinPoint point) throws Throwable {
        System.out.println("before");
        try {
            return point.proceed();
        } finally {
            System.out.println("after");
        }
    }

    @Pointcut("execution(public * com..*.*(..))")
    public void methodsToProfild() {

    }
}