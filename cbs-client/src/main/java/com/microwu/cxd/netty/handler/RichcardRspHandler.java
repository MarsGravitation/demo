package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.struct.AuthenticationFeedbackRspBody;
import com.microwu.cxd.netty.struct.BcnMessage;
import com.microwu.cxd.netty.struct.Body;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.microwu.cxd.netty.enumx.CommandIDEnum.MAAP_RICH_CARD_MONITOR_RSP;

/**
 * Description: Maap业务富媒体卡片消息鉴权反馈
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   16:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class RichcardRspHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BcnMessage message = (BcnMessage) msg;

        // 如果是链路检测消息的响应，则打印消息
        if (message.getHeader() != null && message.getHeader().getCommandId() == MAAP_RICH_CARD_MONITOR_RSP.getParameter()) {
            Body body = message.getBody();
            if (body != null) {
                AuthenticationFeedbackRspBody rspBody = (AuthenticationFeedbackRspBody) body;
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}