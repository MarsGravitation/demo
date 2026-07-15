package com.microwu.cxd.im.handler;

import com.microwu.cxd.im.message.PingMessage;
import com.microwu.cxd.im.message.PongMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   15:32
 * Update History:
 * Author        Time            Content
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 对应的 Channel 通道触发了读闲置事件
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 表示对应的客户端没有发送心跳包，则关闭对应的网络连接
                ctx.channel().close();
                System.out.println("关闭了未发送心跳包的连接...");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端收到心跳包: " + msg);
        if (msg instanceof PingMessage) {
            ctx.channel().writeAndFlush(new PongMessage());
        }
        super.channelRead(ctx, msg);
    }
}
