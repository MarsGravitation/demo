package com.microwu.cxd.network.netty.netty.server;

import com.microwu.cxd.network.netty.netty.NettyConstant;
import com.microwu.cxd.network.netty.netty.codec.NettyMessageDecoder;
import com.microwu.cxd.network.netty.netty.codec.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   15:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyServer {

    public void bind() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                        socketChannel.pipeline().addLast(new NettyMessageEncoder());
                        socketChannel.pipeline().addLast(new LoginAuthRespHandler());
                        socketChannel.pipeline().addLast("HeartBeatHandler",
                                new HeartBeatRespHandler());
                    }
                });

        bootstrap.bind(NettyConstant.REMOTE_IP, NettyConstant.PORT).sync();

    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }
}