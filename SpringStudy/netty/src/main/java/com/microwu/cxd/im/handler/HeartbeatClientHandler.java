package com.microwu.cxd.im.handler;

import com.microwu.cxd.im.message.PingMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   15:38
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class HeartbeatClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 如果当前通道触发了写闲置事件
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                // 标识当前客户端有一段时间未向服务端发送数据了
                // 为了防止服务端关闭当前连接，手动发送一个心跳包
                System.out.println("客户端发送心跳包");
                ctx.channel().writeAndFlush(new PingMessage());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof PongMessage) {
//            System.out.println("客户端收到服务端的心跳包: " + msg);
//        }
//        super.channelRead(ctx, msg);
//    }
}
