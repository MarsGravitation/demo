package com.microwu.cxd.network.netty.netty.client;

import com.microwu.cxd.network.netty.netty.NettyConstant;
import com.microwu.cxd.network.netty.netty.codec.NettyMessageDecoder;
import com.microwu.cxd.network.netty.netty.codec.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   16:02
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyClient {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new NettyMessageDecoder(1024 * 1024, 4, 4));
                            socketChannel.pipeline().addLast("MessageEncoder",
                                    new NettyMessageEncoder());
                            socketChannel.pipeline().addLast("readTimeoutHandler",
                                    new ReadTimeoutHandler(50));
                            socketChannel.pipeline().addLast("LoginAuthHandler",
                                    new LoginAuthReqHandler());
                            socketChannel.pipeline().addLast("HeartBeatHandler",
                                    new HeartBeatReqHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port), new InetSocketAddress(NettyConstant.LOCAL_IP, NettyConstant.LOCAL_PORT)).sync();

            future.channel().closeFuture().sync();
        } finally {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        // 重连
                        connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTE_IP);
    }
}