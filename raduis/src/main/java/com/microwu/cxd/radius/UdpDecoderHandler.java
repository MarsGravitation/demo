package com.microwu.cxd.radius;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinyradius.packet.RadiusPacket;

import java.util.List;

/**
 * UdpDecoderHandler继承MessageToMessageDecoder，
 * 将UDP Client上报的进行decoder解析，UDP Client上报的数据类型为DatagramPacket
 *
 * @author fulei             fulei@microwu.com
 * @date 2019/11/7
 * CopyRight    北京小悟科技有限公司    http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UdpDecoderHandler extends MessageToMessageDecoder<DatagramPacket> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UdpDecoderHandler.class);


    @Autowired
    private RadiusService radiusService;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        LOGGER.info("收到datagramPacket 消息");

        ByteBuf byteBuf = datagramPacket.content();
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        LOGGER.info("data length={}:", data.length);

        RadiusPacket radiusPacket = radiusService.DecodderRadiusPacket(data, "testing123");

        LOGGER.info("收到消息   radiusPacket list={}:", radiusPacket.getAttributes());
//        byteBuf.readBytes(data);
//        String msg = new String(data);
//        LOGGER.info("{}收到消息{}:" + msg);
//        out.add(msg); //将数据传入下一个handler
        out.add(radiusPacket); //将数据传入下一个handler
    }
}
