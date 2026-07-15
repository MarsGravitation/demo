package com.microwu.cxd.network.netty.im.handler;

import com.microwu.cxd.network.netty.im.message.ChatRequestMessage;
import com.microwu.cxd.network.netty.im.message.ChatResponseMessage;
import com.microwu.cxd.network.netty.im.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   15:41
 * Update History:
 * Author        Time            Content
 */
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatRequestMessage chatRequestMessage) throws Exception {
        String to = chatRequestMessage.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage(chatRequestMessage.getFrom(), chatRequestMessage.getContent()));
        } else {
            channelHandlerContext.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或者不在线"));
        }
    }
}
