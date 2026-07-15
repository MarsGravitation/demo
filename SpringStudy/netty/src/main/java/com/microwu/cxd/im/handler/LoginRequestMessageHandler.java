package com.microwu.cxd.im.handler;

import com.microwu.cxd.im.message.LoginRequestMessage;
import com.microwu.cxd.im.message.LoginResponseMessage;
import com.microwu.cxd.im.service.UserServiceFactory;
import com.microwu.cxd.im.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:36
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        System.out.println("msg: " + msg);
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
    }
}
