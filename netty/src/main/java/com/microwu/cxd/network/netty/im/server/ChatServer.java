package com.microwu.cxd.network.netty.im.server;

import com.microwu.cxd.network.netty.im.handler.ChatMessageCodec;
import com.microwu.cxd.network.netty.im.handler.ChatRequestMessageHandler;
import com.microwu.cxd.network.netty.im.handler.LoginRequestMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Description: https://juejin.cn/post/7182845662126407737
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:12
 * Update History:
 * Author        Time            Content
 */
public class ChatServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ChatMessageCodec chatMessageCodec = new ChatMessageCodec();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(chatMessageCodec);
                    ch.pipeline().addLast(new LoginRequestMessageHandler());
                    ch.pipeline().addLast(new ChatRequestMessageHandler());
                }
            });

            Channel channel = serverBootstrap.bind(8888).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            System.out.println("服务端出现错误：" + e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
