package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.core.Connection;
import com.microwu.cxd.netty.enumx.CommandIDEnum;
import com.microwu.cxd.netty.struct.BcnMessage;
import com.microwu.cxd.netty.struct.Header;
import com.microwu.cxd.netty.struct.SequenceNumber;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;

import static com.microwu.cxd.netty.core.Connection.HEARTBEAT_COUNT;
import static com.microwu.cxd.netty.enumx.CommandIDEnum.ENQUIRE_LINK_REP;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   21:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Integer MAX_COUNT = 5;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Integer heartbeatCount = ctx.channel().attr(HEARTBEAT_COUNT).get();
            Connection connection = ctx.channel().attr(Connection.CONNECTION).get();
            if (heartbeatCount > MAX_COUNT) {
                // 关闭连接
                connection.close();
            } else {
                BcnMessage bcnMessage = buildLinkReq(ENQUIRE_LINK_REP);
                ctx.channel().writeAndFlush(bcnMessage).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            System.out.println(System.currentTimeMillis() / 1000 + "," + channelFuture.channel().id() + ": 成功发送心跳连接。。。");
                        } else {
                            System.out.println("发送心跳连接失败。。。");
                            ctx.channel().attr(HEARTBEAT_COUNT).set(heartbeatCount + 1);
                        }
                    }
                });
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 创建链路监测信息
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/26  13:34
     *
     * @param
     * @return  com.microwu.cbs.file.common.domain.struct.BcnMessage
     */
    public static BcnMessage buildLinkReq(CommandIDEnum commandID) {
        BcnMessage message = new BcnMessage();
        Header header = new Header();
        header.setCommandId(commandID.getParameter());
        header.setTransactionID(SequenceNumber.next());
        message.setHeader(header);
        return message;
    }
}