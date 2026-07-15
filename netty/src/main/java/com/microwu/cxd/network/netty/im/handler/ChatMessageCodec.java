package com.microwu.cxd.network.netty.im.handler;

import com.microwu.cxd.network.netty.im.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:59
 * Update History:
 * Author        Time            Content
 */
public class ChatMessageCodec extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf outMsg = ctx.alloc().buffer();
        // 前五个字节作为模数
        byte[] magicNumber = {'c', 'x', 'd'};
        outMsg.writeBytes(magicNumber);
        // 一个字节作为版本号
        outMsg.writeByte(1);
        outMsg.writeByte(0);
        // 一个字节用于表示消息类型
        outMsg.writeByte(msg.getMessageType());
        // 四个字节表示消息序号
        outMsg.writeInt(msg.getSequenceId());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] msgBytes = bos.toByteArray();

        outMsg.writeInt(msgBytes.length);
        outMsg.writeBytes(msgBytes);

        out.add(outMsg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] magicNumber = new byte[3];
        msg.readBytes(magicNumber, 0, 3);
        byte version = msg.readByte();
        byte serializableType  = msg.readByte();
        byte messageType = msg.readByte();
        int sequenceId = msg.readInt();
        int messageLength = msg.readInt();

        byte[] msgBytes = new byte[messageLength];
        msg.readBytes(msgBytes, 0, messageLength);

        ByteArrayInputStream bis = new ByteArrayInputStream(msgBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Message message = (Message) ois.readObject();

        out.add(message);
    }
}
