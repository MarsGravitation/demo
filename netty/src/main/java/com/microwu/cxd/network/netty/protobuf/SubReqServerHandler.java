package com.microwu.cxd.network.netty.protobuf;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/24   16:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        ctx.writeAndFlush(resp(req.getSubReqID()));
        System.out.println(" Receive client response" + msg);
    }

    private SubscribeReqRespProto.SubscribeResp resp(int subReqID) {
        SubscribeReqRespProto.SubscribeResp.Builder builder = SubscribeReqRespProto.SubscribeResp.newBuilder();
        builder.setSubReqId(subReqID);
        builder.setRespCode(0);
        builder.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return builder.build();
    }
}