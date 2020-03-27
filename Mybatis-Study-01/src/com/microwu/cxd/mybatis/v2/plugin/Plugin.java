package com.microwu.cxd.mybatis.v2.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description:
 * Author:   Administration
 * Date:     2019/3/1 13:47
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class Plugin implements InvocationHandler {
    private Object target;
    private Interceptor interceptor;

    /**
     * @Descrip 插件代理者
     * @author 成旭东
     * @date 2019/3/1 13:49
     * @param  * @param target 被代理的Executor
     * @param interceptor plugin插件
     * @return
     */
    public Plugin(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    public static Object wrap(Object obj, Interceptor interceptor){
        Class clazz = obj.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new Plugin(obj, interceptor));
    }

    /**
     * @Descrip 代理的核心方法
     * @author 成旭东
     * @date 2019/3/1 14:23
     * @param  * @param proxy
     * @param method
     * @param args
     * @return java.lang.Object
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 需拦截executor的哪一个方法
        if(interceptor.getClass().isAnnotationPresent(com.microwu.cxd.mybatis.v2.annotation.Method.class)){
            if (method.getName().equals(interceptor.getClass().getAnnotation(com.microwu.cxd.mybatis.v2.annotation.Method .class).value())){
                return interceptor.interceptor(new Invocation(target, method, args));
            }
            return method.invoke(target, method, args);
        }
        return null;
    }
}