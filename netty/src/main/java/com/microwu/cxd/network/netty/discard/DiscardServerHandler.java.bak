package com.microwu.cxd.network.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *  1. DisCardServerHandler 继承自ChannelHandlerAdapter, 这个类实现了
 *  ChannelHandler 接口, ChannelHandler提供了愈多事件处理的接口方法,
 *  然后你可以覆盖这些方法. 现在只需要继承ChannelHandlerAdapter 类
 *  而不是你自己去实现接口方法
 *
 *  2. 这里我们覆盖了chanelRead 事件处理方法. 每当从客户端接受到新的数据时,
 *  这个方法会被调用. 收到的消息类型是ByteBuf
 *
 *  3.为了实现DsisCard协议, 处理器不得不忽略所有接收到的消息. ByteBuf 是一个
 *  引用计数对象, 这个对象必须显式的释放. 请记住处理器的职责是释放所有
 *  传递到处理器的引用计数对象. 通过调用release 释放
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/26   10:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
//        try {
//            while(in.isReadable()) {
//                System.out.println((char) in.readByte());
//                System.out.flush();
//            }
//        }finally {
//            ((ByteBuf) msg).release();
//        }
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}