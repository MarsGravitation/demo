package com.microwu.cxd.network.netty.blog.rpc.kernel.remote;

import com.microwu.cxd.network.netty.blog.rpc.kernel.netty.client.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description: 远程处理器
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;
    private int port;
    private Class interfaces;

    public RemoteInvocationHandler(String host, int port, Class interfaces) {
        this.host = host;
        this.port = port;
        this.interfaces = interfaces;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 封装消息
        RpcContext rpcContext = new RpcContext();
        rpcContext.setClassName(interfaces.getName());
        rpcContext.setMethodName(method.getName());
        rpcContext.setTypes(method.getParameterTypes());
        rpcContext.setParams(args);

        NettyClient nettyClient = new NettyClient(host, port);
        nettyClient.connect();

        return nettyClient.sendData(rpcContext);
    }
}