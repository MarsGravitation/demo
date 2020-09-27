package com.microwu.cxd.network.netty.blog.rpc.kernel.remote;

import java.lang.reflect.Proxy;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RpcProxyFactory<T> {

    public T factoryRemoteInvoker(String host, int port, Class interfaces) {
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces}, new RemoteInvocationHandler(host, port, interfaces));
    }
}