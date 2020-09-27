package com.microwu.cxd.network.netty.netty.codec;

import com.microwu.cxd.network.netty.netty.struct.Header;
import com.microwu.cxd.network.netty.netty.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   15:10
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 1. 调用父类获取包
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if(frame == null) {
            return null;
        }

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        // 附件格式大于 0，需要解码操作
        if(size > 0) {
            HashMap<String, Object> attch = new HashMap<>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for(int i = 0; i < size; i++) {
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                // 解码
                attch.put(key, marshallingDecoder.decode(frame));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attch);
        }

        // 对于 ByteBuf 来说，读一个数据，就会少一个数据，所以读完 header，剩下就是 body 了
        // 如果大于四个字节，说明有数据，4 个字节是内容长度的占位
        if(frame.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(frame));
        }
        message.setHeader(header);
        return message;
    }
}