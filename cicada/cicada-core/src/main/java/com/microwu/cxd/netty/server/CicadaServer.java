package com.microwu.cxd.netty.server;

import com.microwu.cxd.netty.server.config.AppConfig;
import com.microwu.cxd.netty.server.handler.HttpHandler;
import com.microwu.cxd.netty.server.utils.LoggerBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   11:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CicadaServer {

    private final static Logger LOGGER = LoggerBuilder.getLogger(CicadaServer.class);

    private static final int BOSS_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static EventLoopGroup boss = new NioEventLoopGroup(BOSS_SIZE);
    private static EventLoopGroup work = new NioEventLoopGroup();

    public static void start(Class<?> clazz, String path) {
        long start = System.currentTimeMillis();

        // init application
        AppConfig appConfig = AppConfig.getInstance();
        appConfig.setRootPackageName(clazz.getPackage().getName());
        appConfig.setRootPath(path);

        int port = appConfig.getPort();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new HttpHandler())
                                    .addLast("logging", new LoggingHandler(LogLevel.INFO));
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            if(future.isSuccess()) {
                long end = System.currentTimeMillis();
                LOGGER.info("Cicada started on port: {}.cost {} ms", port, end - start);
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}