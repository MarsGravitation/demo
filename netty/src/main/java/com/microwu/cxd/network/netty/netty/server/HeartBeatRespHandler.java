package com.microwu.cxd.network.netty.netty.server;

import com.microwu.cxd.network.netty.netty.MessageType;
import com.microwu.cxd.network.netty.netty.struct.Header;
import com.microwu.cxd.network.netty.netty.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   15:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //　返回心跳应答信息
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            NettyMessage heatBeat = builderHeatBeat();
            ctx.writeAndFlush(heatBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage builderHeatBeat() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        nettyMessage.setHeader(header);
        return nettyMessage;
    }
}