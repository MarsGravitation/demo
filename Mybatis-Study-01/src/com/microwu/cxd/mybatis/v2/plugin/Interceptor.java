package com.microwu.cxd.mybatis.v2.plugin;

public interface Interceptor {
    Object interceptor(Invocation invocation) throws Throwable;

    Object plugin(Object object);
}
