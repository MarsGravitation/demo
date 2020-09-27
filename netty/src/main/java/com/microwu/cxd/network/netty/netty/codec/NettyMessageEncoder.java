package com.microwu.cxd.network.netty.netty.codec;

import com.microwu.cxd.network.netty.netty.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   14:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if(msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }

        ByteBuf sendBuf = Unpooled.buffer();
        // 1. 处理 header
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt((msg.getHeader().getLength()));
        sendBuf.writeLong((msg.getHeader().getSessionID()));
        sendBuf.writeByte((msg.getHeader().getType()));
        sendBuf.writeByte((msg.getHeader().getPriority()));

        // 2. 对附件信息进行编码
        // 如果 attachment 的长度为 0，没有可选附件，则将长度 设置为 0
        // 如果 attachment 长度大于 0，则需要编码，规则：
        // 首先对附件的个数进行编码
        sendBuf.writeInt((msg.getHeader().getAttachment().size()));
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        // 然后对 key 进行编码，先编码长度，然后再将它转换成 byte 数组
        // 之后编码内容
        for(Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value, sendBuf);
        }
        key = null;
        value = null;
        if(msg.getBody() != null) {
            // 使用 MarshallingEncoder
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        } else {
            // 如果没有数据 则进行补位 为了方便后续的 decoder 操作
            sendBuf.writeInt(0);
        }

        // 最后我们要获取整个数据包的总长度 也就是 header + body 进行对 header length 的设置
        // 第一个参数是长度属性的索引位置
        // 这里必须要 -8 个字节，公式：数据包长度 =  lengthFieldOffset + lengthFieldLength + 长度域的值 + lengthAdjustment
        // 长度域的值 = 数据包长度 - lengthFieldOffset - lengthFieldLength - lengthAdjustment
        sendBuf.setIndex(4, sendBuf.readableBytes() - 8);

        out.add(sendBuf);
    }

//    /**
//     * 编码器
//     *
//     * @author   chengxudong               chengxudong@microwu.com
//     * @date    2020/8/13  13:37
//     *
//     * @param   	ctx
//     * @param 		msg
//     * @param 		sendBuf
//     * @return  void
//     */
//    @Override
//    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
//        if(msg == null || msg.getHeader() == null) {
//            throw new Exception("The encode message is null");
//        }
//
//        // 1. 处理 header
//        sendBuf.writeInt(msg.getHeader().getCrcCode());
//        sendBuf.writeInt((msg.getHeader().getLength()));
//        sendBuf.writeLong((msg.getHeader().getSessionID()));
//        sendBuf.writeByte((msg.getHeader().getType()));
//        sendBuf.writeByte((msg.getHeader().getPriority()));
//
//        // 2. 对附件信息进行编码
//        // 如果 attachment 的长度为 0，没有可选附件，则将长度 设置为 0
//        // 如果 attachment 长度大于 0，则需要编码，规则：
//        // 首先对附件的个数进行编码
//        sendBuf.writeInt((msg.getHeader().getAttachment().size()));
//        String key = null;
//        byte[] keyArray = null;
//        Object value = null;
//        // 然后对 key 进行编码，先编码长度，然后再将它转换成 byte 数组
//        // 之后编码内容
//        for(Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
//            key = param.getKey();
//            keyArray = key.getBytes("UTF-8");
//            sendBuf.writeInt(keyArray.length);
//            sendBuf.writeBytes(keyArray);
//            value = param.getValue();
//            marshallingEncoder.encode(value, sendBuf);
//        }
//        key = null;
//        value = null;
//        if(msg.getBody() != null) {
//            // 使用 MarshallingEncoder
//            marshallingEncoder.encode(msg.getBody(), sendBuf);
//        } else {
//            // 如果没有数据 则进行补位 为了方便后续的 decoder 操作
//            sendBuf.writeInt(0);
//        }
//
//        // 最后我们要获取整个数据包的总长度 也就是 header + body 进行对 header length 的设置
//        // 第一个参数是长度属性的索引位置
//        // 这里必须要 -8 个字节，公式：数据包长度 =  lengthFieldOffset + lengthFieldLength + 长度域的值 + lengthAdjustment
//        // 长度域的值 = 数据包长度 - lengthFieldOffset - lengthFieldLength - lengthAdjustment
//        sendBuf.setIndex(4, sendBuf.readableBytes() - 8);
//    }
}