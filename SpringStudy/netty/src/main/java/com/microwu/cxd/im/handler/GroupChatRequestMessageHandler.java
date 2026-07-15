package com.microwu.cxd.im.handler;

import com.microwu.cxd.im.message.GroupChatRequestMessage;
import com.microwu.cxd.im.message.GroupChatResponseMessage;
import com.microwu.cxd.im.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   10:57
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupChatRequestMessage groupChatRequestMessage) throws Exception {
        List<Channel> channelList = GroupSessionFactory.getSession().getMemberChannel(groupChatRequestMessage.getGroupName());

        for (Channel channel: channelList) {
            channel.writeAndFlush(new GroupChatResponseMessage(groupChatRequestMessage.getFrom(), groupChatRequestMessage.getContent()));
        }
    }
}
