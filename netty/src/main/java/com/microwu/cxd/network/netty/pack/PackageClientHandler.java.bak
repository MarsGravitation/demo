package com.microwu.cxd.network.netty.pack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/27   10:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PackageClientHandler extends ChannelHandlerAdapter {

    private int counter;

    private byte[] reqByte;

    public PackageClientHandler() {
        reqByte = "hello, i am client\r\n".getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i < 100; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(reqByte);
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg + ", the counter: " + ++counter);
    }
}