package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.client;

import com.microwu.cxd.network.netty.blog.rpc.kernel.netty.hessian.HessianDecode;
import com.microwu.cxd.network.netty.blog.rpc.kernel.netty.hessian.HessianEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.concurrent.CountDownLatch;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyClientHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private CountDownLatch latch;
    private NettyClientHandler nettyClientHandler;

    public NettyClientHandlerInitializer(CountDownLatch latch) {
        this.latch = latch;
    }

    public Object getServerResult() {
        return nettyClientHandler.getResult();
    }

    public void reLatch(CountDownLatch latch) {
        this.nettyClientHandler.reLatch(latch);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        nettyClientHandler = new NettyClientHandler(latch);

        HessianEncode hessianEncode = new HessianEncode();
        HessianDecode hessianDecode = new HessianDecode();

        // 在消息头前加消息的长度
        LengthFieldPrepender fieldPrepender = new LengthFieldPrepender(2);

        // 和编解码的顺序无关，但是编解码之前必须解决粘包、半包问题
        // 出站
        ch.pipeline().addLast(fieldPrepender);
        ch.pipeline().addLast(hessianEncode);

        // 入站
        // https://blog.csdn.net/u010853261/category_6567026.html
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
        ch.pipeline().addLast((hessianDecode));
        ch.pipeline().addLast(nettyClientHandler);
    }
}