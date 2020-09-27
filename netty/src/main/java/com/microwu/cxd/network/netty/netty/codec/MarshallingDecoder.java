package com.microwu.cxd.network.netty.netty.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   15:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MarshallingDecoder {
    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception {
        // 1. 首先读取 4 个长度，实际 body 的内容长度
        int objectSize = in.readInt();
        // 2. 获取实际 body 的缓冲内容
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
        try {
            unmarshaller.start(input);
            // 3. 转换
            Object object = unmarshaller.readObject();
            unmarshaller.finish();
            // 4. 读取完毕，更新当前读取起始位置
            // 因为使用 slice 方法，原 buf 的位置还在 readIndex 上，故需要重新设置一下
            in.readerIndex(in.readerIndex() + objectSize);
            return object;
        }finally {
            unmarshaller.close();
        }
    }

}