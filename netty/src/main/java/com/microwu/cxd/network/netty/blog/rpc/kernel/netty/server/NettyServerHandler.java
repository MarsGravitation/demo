package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.server;

import com.microwu.cxd.network.netty.blog.rpc.kernel.registry.Registry;
import com.microwu.cxd.network.netty.blog.rpc.kernel.remote.RpcContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:02
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

@ChannelHandler.Sharable // 此 handler 能在多个线程间贡献，那么实现也必须是线程安全的
public class NettyServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcContext model = (RpcContext) msg;

        Class clazz = null;
        if (Registry.map.containsKey(model.getClassName())) {
            clazz = Registry.map.get(model.getClassName());
        }

        Object result = null;
        Method method = clazz.getMethod(model.getMethodName(), model.getTypes());
        result = method.invoke(clazz.newInstance(), model.getParams());

        ctx.channel().writeAndFlush(result);
    }
}