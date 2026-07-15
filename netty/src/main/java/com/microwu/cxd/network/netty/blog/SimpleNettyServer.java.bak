package com.microwu.cxd.network.netty.blog;

import com.microwu.cxd.network.netty.blog.pack.PackServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * Description:
 *  1. 初始化用户 Acceptor 的 主线程池 以及用于 IO 工作的 从线程池
 *  2. 初始化 ServerBootstrap 实例，此实例是 netty 服务端应用开发的入口
 *  3. 通过 ServerBootstrap 的 group 设置初始化的主从线程池
 *  4. 指定通道 Channel 的类型，由于是服务端，故而是 Nio
 *  5. 设置 ServerSocketChannel 的处理器
 *  6. 设置子通道也就是 SocketChannel 的处理器，其内部是实际业务开发的主战场
 *  7. 配置子通道也就是 SocketChannel 的选项
 *  8. 绑定并侦听某个端口
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   11:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SimpleNettyServer {

    public void bind(int port) throws InterruptedException {
        // 服务器端应用程序使用两个 NioEventLoopGroup 创建两个 EventLoop 的组
        // EventLoop 这个相当于一个处理线程，是 Netty 接受请求和处理 IO 请求的线程
        // 主程序组，用户接受客户端的连接，但是不做任何处理，跟老板一样，不做事
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程组，当 boss 接受连接并注册被接受的连接到 worker 时，处理被接受连接的流量
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // netty 服务器启动类的创建，辅助工具类，用于服务器通道的一系列配置
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 使用了多少线程以及如何将它们映射到创建的通道取决于 EventLoopGroup 实现，
            // 甚至可以通过构造函数进行配置
            // 设置循环线程组，前者用于处理客户端连接事件，后者用于处理网络 IO
            serverBootstrap.group(bossGroup, workerGroup)
                    // 指定 NIO 模式
                    .channel(NioServerSocketChannel.class)
                    // 子处理器也可以通过下面的内部方法实现
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 子处理器，用于处理 workerGroup
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // demo
//                            socketChannel.pipeline().addLast(new SimpleNettyServerHandler());
                            // 粘包、半包
                            socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(21));
                            socketChannel.pipeline().addLast(new PackServerHandler());
                        }
                    });

            // 启动 Server，绑定端口，开始接受进来的连接，设置 8080 为启动的端口，同时启动方式为同步
            ChannelFuture future = serverBootstrap.bind(8080).sync();

            System.out.println("Server start");
            // 监听关闭 channel ，等待服务器 socket 关闭，设置为同步方式
            future.channel().closeFuture().sync();
        } finally {
            // 退出线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new SimpleNettyServer().bind(8080);
    }

}