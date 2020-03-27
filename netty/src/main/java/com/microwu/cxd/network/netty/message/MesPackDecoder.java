package com.microwu.cxd.network.netty.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Description: 解码器
 *  1. 从byteBuf 中获取byte数组
 *  2. messagePack.read 反序列化成对象
 *  3. 将解码的对象加入解码列表list中
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/27   11:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MesPackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
//        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        byteBuf.readBytes(array);
        MessagePack messagePack = new MessagePack();
        list.add(messagePack.read(array));
    }
}