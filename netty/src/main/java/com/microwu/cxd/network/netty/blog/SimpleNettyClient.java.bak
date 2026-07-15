package com.microwu.cxd.network.netty.blog;

import com.microwu.cxd.network.netty.blog.pack.PackClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   12:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SimpleNettyClient {

    public void connect(String host, int port) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            // 客户端启动类程序
            Bootstrap bootstrap = new Bootstrap();
            // EventLoop 的组
            bootstrap.group(worker);
            // 用于构造 socketChannel 工厂
            bootstrap.channel(NioSocketChannel.class);
            // 设置选项
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            // 自定义客户端 handler
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 入门案例
//                    ch.pipeline().addLast(new SimpleNettyClientHandler());
                    // 粘包、半包问题
                    // 定长
//                    ch.pipeline().addLast(new FixedLengthFrameDecoder(18));
                    // 分隔符
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("123".getBytes())));
                    ch.pipeline().addLast(new PackClientHandler());
                }
            });

            // 开启客户端监听，连接到远程节点，阻塞等到直到连接完成
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 阻塞等待数据，直到 Channel 关闭（客户端关闭）
            channelFuture.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleNettyClient simpleNettyClient = new SimpleNettyClient();
        simpleNettyClient.connect("localhost", 8080);
    }

}