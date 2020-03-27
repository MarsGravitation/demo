package com.microwu.cxd.mybatis.v2.plugin.customPlugin;

import com.microwu.cxd.mybatis.v2.plugin.Interceptor;
import com.microwu.cxd.mybatis.v2.plugin.Invocation;
import com.microwu.cxd.mybatis.v2.plugin.Plugin;

/**
 * Description: 测试插件
 * Author:   Administration
 * Date:     2019/3/1 14:32
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class TestPlugin implements Interceptor {
    @Override
    public Object interceptor(Invocation invocation) throws Throwable {
        String statement = (String) invocation.getArgs()[0];
        String parameter = (String) invocation.getArgs()[1];
        Class pojo = (Class) invocation.getArgs()[2];
        System.out.println("-----plugin生效-----");
        // 这里执行原executor的方法
        return invocation.proceed();
    }

    /**
     * @Descrip 此方法实际上就是给插件target做一个动态代理
     * @author 成旭东
     * @date 2019/3/1 14:34
     * @param  * @param object
     * @return java.lang.Object
     */
    @Override
    public Object plugin(Object object) {
        return Plugin.wrap(object, this);
    }
}