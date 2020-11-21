package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.struct.BcnMessage;
import com.microwu.cxd.netty.struct.Body;
import com.microwu.cxd.netty.struct.ResultInfoNotifyRspBody;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.microwu.cxd.netty.enumx.CommandIDEnum.MAAP_RESULT_INFO_NOTIFY_REQ;

/**
 * Description: 等待人工审核结果通知 MaaP 服务接口
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   16:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class ResultNotifyRspHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BcnMessage message = (BcnMessage) msg;

        // 如果是链路检测消息的响应，则打印消息
        if (message.getHeader() != null && message.getHeader().getCommandId() == MAAP_RESULT_INFO_NOTIFY_REQ.getParameter()) {
            Body body = message.getBody();
            if (body != null) {
                ResultInfoNotifyRspBody rspBody = (ResultInfoNotifyRspBody) body;
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