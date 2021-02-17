package com.microwu.cxd.server.init;

import com.microwu.cxd.common.protocol.CIMRequestProto;
import com.microwu.cxd.server.handle.CimServerHandle;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   9:48
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CimServerInitializer extends ChannelInitializer<Channel> {

    private final CimServerHandle cimServerHandle = new CimServerHandle();
    private final ByteBuf delimiter = Unpooled.copiedBuffer(".".getBytes());

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline()
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(CIMRequestProto.CIMReqProtocol.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new IdleStateHandler(30, 0, 0))
                .addLast(cimServerHandle);

    }
}