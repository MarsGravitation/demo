package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.struct.AuthenticationFeedbackRspBody;
import com.microwu.cxd.netty.struct.BcnMessage;
import com.microwu.cxd.netty.struct.Body;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.microwu.cxd.netty.enumx.CommandIDEnum.MAAP_CHATBOT_MULTI_MONITOR_RSP;

/**
 * Description: Maap 业务 Chatbot 上传元素鉴权反馈
 *
 *  存在的问题：获取 spring 容器的对象时，报空指针异常，这里我使用 get 方法获取，因为环境问题，
 *      没办法测试是否可用，等联测的时候测试，如果此方法不可以的话，只能通过构造函数传递参数
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   13:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class ChatbotMultiRspHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BcnMessage message = (BcnMessage) msg;

        // 如果是链路检测消息的响应，则打印消息
        if (message.getHeader() != null && message.getHeader().getCommandId() == MAAP_CHATBOT_MULTI_MONITOR_RSP.getParameter()) {
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