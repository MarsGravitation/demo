package com.microwu.cxd.network.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/29   11:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HttpFileServer {

    private static final String DEFAULT_URL = "/src/main/java";

    public void run(final int port, final String url) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // HTTP 请求消息解码器
                            socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            // HttpObjectAggregator 将多个消息转换成单一的FullHttpRequest 或 FullHttpResponse
                            // 原因是HTTP 解码器在每个HTTP消息中生成多个消息对象
                            socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                            // HTTP 响应解码器
                            socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            // 支持一部发送大的码流, 文件传输
                            socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            socketChannel.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture f = serverBootstrap.bind(8080).sync();

            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpFileServer().run(8080, DEFAULT_URL);
    }
}