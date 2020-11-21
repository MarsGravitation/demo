package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.enumx.CommandIDEnum;
import com.microwu.cxd.netty.struct.BcnMessage;
import com.microwu.cxd.netty.struct.Body;
import com.microwu.cxd.netty.struct.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   14:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 1. 调用父类（LengthFieldBasedFrameDecoder） 方法
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        if (frame == null) {
            return null;
        }

        BcnMessage message = new BcnMessage();
        Header header = new Header();
        header.setLength(frame.readInt());
        header.setCommandId(frame.readInt());
        header.setTransactionID(frame.readInt());
        header.setVersion(frame.readInt());
        message.setHeader(header);

        if (frame.readableBytes() > 0) {
            // 如果存在 body
            // 这里使用了反射，如果性能有所影响，可以直接 new 对象
            String classString = CommandIDEnum.MAP.get(header.getCommandId());
            Class<?> clazz = Class.forName(classString);
            Body body = (Body) clazz.newInstance();
            body.readBody(frame);
            message.setBody(body);
        }

        ReferenceCountUtil.release(frame);
        return message;
    }
}