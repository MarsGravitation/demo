package com.microwu.cxd.network.netty.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Description:
 *  它负责将Object类型的POJO 对象编码为byte数组, 然后写入ByteBuf
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/27   11:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MsgPackEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object user, ByteBuf byteBuf) throws Exception {
        MessagePack messagePack = new MessagePack();
        byte[] raw = messagePack.write(user);
        byteBuf.writeBytes(raw);
    }
}