package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.struct.BcnMessage;
import com.microwu.cxd.netty.struct.Body;
import com.microwu.cxd.netty.struct.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Description: 消息编码器
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   11:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MessageEncoder extends MessageToByteEncoder<BcnMessage> {

    /**
     * message  --> ByteBuf
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/26  11:32
     *
     * @param   	channelHandlerContext
     * @param 		message
     * @param 		byteBuf
     * @return  void
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BcnMessage message, ByteBuf byteBuf) throws Exception {
        if (message == null || message.getHeader() == null) {
            throw new Exception("编码失败，没有数据信息");
        }

        // header
        Header header = message.getHeader();
        byteBuf.writeInt(header.getLength());
        byteBuf.writeInt(header.getCommandId());
        byteBuf.writeInt(header.getTransactionID());
        byteBuf.writeInt(header.getVersion());

        // body
        Body body = message.getBody();
        if (body != null) {
            body.writeBody(byteBuf);
        }

        // 获取 length
        int length = byteBuf.readableBytes();
        // 发送数据包的长度 = 长度域的值 + lengthFieldOffset + lengthFieldLength + lengthAdjustment
        // 这里需要这样计算吗？这个计算方式是对应解码器的计算方式
        // 这里我感觉 length 不能变，要调整解码器的参数
        byteBuf.setInt(0, length);
    }
}