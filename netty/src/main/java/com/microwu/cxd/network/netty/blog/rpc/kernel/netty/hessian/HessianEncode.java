package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.hessian;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HessianEncode extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] write = HessianSerializerUtils.serialize(msg);
        out.writeBytes(write);
    }
}