package com.microwu.cxd.network.netty.pool;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/8   14:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    static AtomicInteger count = new AtomicInteger(1);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(count.getAndIncrement() + " : " + msg);
        ctx.writeAndFlush("Welcome to Netty.$_");
    }
}