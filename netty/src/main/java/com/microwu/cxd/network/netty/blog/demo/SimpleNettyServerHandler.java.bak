package com.microwu.cxd.network.netty.blog.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *  1. EventLoop、EventLoopGroup
 *  2. AbstractBootstrap
 *  3. ChannelPipeline
 *      3.1 ChannelHandler
 *      3.2 ChannelPipeline
 *          3.2.1 概述
 *          3.2.2 常用方法
 *          3.2.3 入站出站 handler 执行顺序
 *          3.2.4 ChannelHandler 协作规则
 *      3.3 ChannelHandlerContext
 *          ChannelPipeline 通过 ChannelHandlerContext 来管理
 *          默认实现 DefaultChannelContext 内部维护了一个双向链表
 *      3.4 几者关系
 *          一个 Channel 包含一个 ChannelPipeline，创建 Channel 时会自动创建一个 ChannelPipeline
 *          每个 ChannelPipeline 可以包含多个 ChannelHandler，ChannelHandler 与 ChannelPipeline 的桥梁是 ChannelHandlerContext
 *  4. ByteBuf
 *
 *
 * https://www.cnblogs.com/jing99/category/1669280.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   11:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SimpleNettyServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("SimpleNettyServerHandler.channelRead");

        ByteBuf result = (ByteBuf) msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);

        System.out.println("client said: " + new String(bytes));
        // 释放资源
        result.release();

        // 向客户端发送消息
        String response = "hello client!";
        ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
        encoded.writeBytes(response.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}