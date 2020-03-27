package com.microwu.cxd.spring.heart;

import com.microwu.cxd.spring.domain.CustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/23   14:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class EchoClientHandler extends ChannelHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if(idleStateEvent.state() == IdleState.WRITER_IDLE) {
                System.out.println("已经10秒没法送消息了!!!");
                // 向服务端发送消息
                CustomProtocol protocol = new CustomProtocol(1L, "我快死了!!!");
                ctx.writeAndFlush(protocol).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("========= 客户端收到消息 =========");
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println(buffer.toString(CharsetUtil.UTF_8));
    }
}