package com.microwu.cxd.network.netty.message;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/28   15:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MesPackClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User[] users = users();
        for(User user : users) {
            ctx.writeAndFlush(user);
        }
    }

    private User[] users() {
        User[] users = new User[10];
        for(int i = 0; i < users.length; i++) {
            User user = new User((long)i, "cxd" + i, "123456");
            users[i] = user;
        }
        return users;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive: " + msg);
    }
}