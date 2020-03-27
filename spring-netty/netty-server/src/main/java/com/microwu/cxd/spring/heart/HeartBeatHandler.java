package com.microwu.cxd.spring.heart;

import com.microwu.cxd.spring.common.NettySocketHolder;
import com.microwu.cxd.spring.domain.CustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/23   14:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class HeartBeatHandler extends ChannelHandlerAdapter {
    private static final ByteBuf HEART_BEAT  =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(new CustomProtocol(123456L,"pong").toString(), CharsetUtil.UTF_8));


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettySocketHolder.remove((NioSocketChannel) ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if(idleStateEvent.state() == IdleState.READER_IDLE) {
                System.out.println("已经5秒没收到消息了!!!");
                // 向客户端发送消息
                ctx.writeAndFlush(HEART_BEAT).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CustomProtocol customProtocol = (CustomProtocol) msg;
        System.out.println("服务端接受消息: " + customProtocol);
        // 保存客户端与Channel之间的关系
        NettySocketHolder.put(customProtocol.getId(), (NioSocketChannel) ctx.channel());
    }
}