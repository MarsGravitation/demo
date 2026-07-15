package com.microwu.cxd.network.netty.marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/20   9:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("===== 服务端读通道可读 =====");
        ProtocolMessage message = (ProtocolMessage) msg;
        System.out.println(message);
        ctx.writeAndFlush(new ProtocolMessage(1, "500", "服务端异常"));
    }
}