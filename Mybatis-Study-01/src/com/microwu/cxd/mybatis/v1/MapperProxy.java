package com.microwu.cxd.mybatis.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description: Mapper的代理者
 * Author:   Administration
 * Date:     2019/2/26 17:12
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class MapperProxy implements InvocationHandler {
    private CustomSqlSession customSqlSession;

    public MapperProxy(CustomSqlSession customSqlSession) {
        this.customSqlSession = customSqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(customSqlSession.getConfiguration().hasStatement(method.getDeclaringClass().getName() + "." + method.getName())){
            String sql = customSqlSession.getConfiguration().getStatement(method.getDeclaringClass().getName() + "." + method.getName());
            return customSqlSession.selectOne(sql, args[0].toString());
        }
        return method.invoke(proxy, args);
    }
}