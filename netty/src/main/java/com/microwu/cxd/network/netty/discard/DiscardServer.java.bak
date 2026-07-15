package com.microwu.cxd.network.netty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/26   10:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DiscardServer {
    private final int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 1.
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //2.
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    //3.
                    .channel(NioServerSocketChannel.class)
                    // 4. 这里的事件处理类进程会被用来处理一个最近的已接收到的Channel
                    // ChannelInitializer 是一个特殊的处理类, 它的目的是帮助使用者
                    // 配置一个新的频道. 也许你想通过增加一些处理类来配置一个新的
                    // Channel或它对应的ChannelPipeline来实现你的网络程序
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    //5. 你可以设置通道实现的配置参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 6.
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 7. 绑定端口, 启动服务
            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new DiscardServer(port).run();
    }
}