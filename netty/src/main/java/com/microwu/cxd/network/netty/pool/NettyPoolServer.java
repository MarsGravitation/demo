package com.microwu.cxd.network.netty.pool;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/8   14:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyPoolServer {

    public void bind(int port) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiter  = Unpooled.copiedBuffer("$_".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                                    .addLast(new StringEncoder())
                                    .addLast(new StringDecoder())
                                    .addLast(new NettyServerHandler());


                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();

        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new NettyPoolServer().bind(8080);
    }

}