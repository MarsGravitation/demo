package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.struct.BcnMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.microwu.cxd.netty.enumx.CommandIDEnum.ENQUIRE_LINK_REP;
import static com.microwu.cxd.netty.enumx.CommandIDEnum.ENQUIRE_LINK_REQ;

/**
 * Description: 链路检测请求
 *  这里相当于心跳包，每 20s 发送一次，检测链路
 *  这里我的思路：
 *      1. TCP 连接成功后，起一个定时任务开始进行链路检测 - 发送心跳包
 *      2. 可以添加一个空闲检测 handler，如果超过一定时间空闲后，发送一个心跳包
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   13:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class EnquireLinkReqHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BcnMessage message = (BcnMessage) msg;

        // 如果是链路检测消息的请求，意味着服务器发送心跳包，返回响应
        if (message.getHeader() != null && message.getHeader().getCommandId() == ENQUIRE_LINK_REQ.getParameter()) {
            // 响应消息
            BcnMessage repMessage = HeartbeatHandler.buildLinkReq(ENQUIRE_LINK_REP);
            ctx.writeAndFlush(repMessage);
        } else if (message.getHeader() != null && message.getHeader().getCommandId() == ENQUIRE_LINK_REP.getParameter()) {
            // 如果是链路检测消息的响应，则打印消息
            System.out.println(message);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

}