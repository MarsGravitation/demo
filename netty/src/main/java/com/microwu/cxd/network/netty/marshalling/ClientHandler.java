package com.microwu.cxd.network.netty.marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/20   9:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("====== 读通道就绪 =====");
        for(int i = 0; i < 10; i++) {
            ProtocolMessage message = new ProtocolMessage(i, "200", "你好, 服务端!!!");
            ctx.write(message);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("===== 客户端 读通道可读 =====");
        ProtocolMessage message = (ProtocolMessage) msg;
        System.out.println(message);
    }
}