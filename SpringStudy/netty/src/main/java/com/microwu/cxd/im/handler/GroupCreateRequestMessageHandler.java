package com.microwu.cxd.im.handler;

import com.microwu.cxd.im.message.GroupCreateRequestMessage;
import com.microwu.cxd.im.message.GroupCreateResponseMessage;
import com.microwu.cxd.im.session.Group;
import com.microwu.cxd.im.session.GroupSession;
import com.microwu.cxd.im.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   10:43
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupCreateRequestMessage groupCreateRequestMessage) throws Exception {
        String groupName = groupCreateRequestMessage.getGroupName();
        Set<String> members = groupCreateRequestMessage.getMembers();
        GroupSession session = GroupSessionFactory.getSession();
        Group group = session.createGroup(groupName, members);
        if (group == null) {
            channelHandlerContext.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));

            List<Channel> channels = session.getMemberChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入" + groupName));
            }
        } else {
            channelHandlerContext.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "已经存在"));
        }
    }
}
