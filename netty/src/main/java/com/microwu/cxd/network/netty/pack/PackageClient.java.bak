package com.microwu.cxd.network.netty.pack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/27   10:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PackageClient {

    public void run(String host, int port) {
        //创建线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            // 创建启动器
            Bootstrap bootstrap = new Bootstrap();
            // 配置启动器
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // LineBasedFrameDecoder 根据\r\n 判断一行
                            // StringDecoder 将字节转换成 String
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new PackageClientHandler());
                        }
                    });

            // 连接服务器
            ChannelFuture f = bootstrap.connect(host, port).sync();

            // 关闭链路
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            group.shutdownGracefully();

        }
    }

    public static void main(String[] args) {
        new PackageClient().run("127.0.0.1", 8080);
    }
}