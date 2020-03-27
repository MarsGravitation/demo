package com.microwu.cxd.spring.heart;

import com.microwu.cxd.spring.domain.CustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/23   15:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class HeartbeatEncoder extends MessageToByteEncoder<CustomProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CustomProtocol customProtocol, ByteBuf byteBuf) throws Exception {
        byteBuf.writeLong(customProtocol.getId());
        byteBuf.writeBytes(customProtocol.getContent().getBytes());
    }
}