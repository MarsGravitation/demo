package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.hessian;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HessianDecode extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] array;
        final int length = msg.readableBytes();
        array = new byte[length];
        msg.getBytes(msg.readerIndex(), array, 0, length);
        out.add(HessianSerializerUtils.deserialize(array));
    }
}