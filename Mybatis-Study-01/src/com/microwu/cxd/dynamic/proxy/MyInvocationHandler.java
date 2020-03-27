package com.microwu.cxd.dynamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description:
 * Author:   Administration
 * Date:     2019/2/27 13:43
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class MyInvocationHandler implements InvocationHandler {
    // 目标对象
    private UserService target;

    public MyInvocationHandler(UserService target) {
        this.target = target;
    }

    /**
     * @Descrip 执行目标方法
     * @author 成旭东
     * @date 2019/2/27 13:50
     * @param  * @param proxy 代理对象
     * @param method 目标方法
     * @param args 方法参数
     * @return java.lang.Object 执行方法后的返回值
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        System.out.println(result);
        after();
        return result;
    }

    private void before(){
        System.out.println("-----before()-----");
    }

    private void after(){
        System.out.println("-----after()-----");
    }

    /**
     * @Descrip 获取代理对象
     * @author 成旭东
     * @date 2019/2/27 13:53
     * @param  * @param
     * @return java.lang.Object
     */
    public Object getProxy(){
        return Proxy.newProxyInstance(MyInvocationHandler.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }
}