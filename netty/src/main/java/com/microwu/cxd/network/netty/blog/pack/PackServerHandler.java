package com.microwu.cxd.network.netty.blog.pack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * Description:
 *  解决办法：
 *      1. 分隔符
 *      2. 定长
 *      3. 消息头
 *
 * https://www.cnblogs.com/jing99/p/12554739.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   14:29
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PackServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        System.out.println("Server Accept: " + new String(bytes, CharsetUtil.UTF_8));
        ctx.channel().write(msg);
    }
}