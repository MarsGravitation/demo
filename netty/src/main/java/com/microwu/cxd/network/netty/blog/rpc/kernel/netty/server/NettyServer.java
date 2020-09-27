package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.server;

import com.microwu.cxd.network.netty.blog.rpc.kernel.netty.hessian.HessianDecode;
import com.microwu.cxd.network.netty.blog.rpc.kernel.netty.hessian.HessianEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   14:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyServer {

    public void bind(int port) throws InterruptedException {
        NettyServerHandler serverHandler = new NettyServerHandler();
        HessianEncode hessianEncode = new HessianEncode();
        HessianDecode hessianDecode = new HessianDecode();

        LengthFieldPrepender fieldPrepender = new LengthFieldPrepender(2);

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 1. 入站、出站顺序
                    // InboundHandler 顺序执行，OutboundHandlerHandler 逆序执行
                    // 2. 协作规则
                    // 2.1 业务执行后需要 ChannelHandlerContext.fire*() 或者 Channel*Handler.super*()，否则不会传递到下一个 handler（据我观察，这个适用于 in）
                    // 2.2 如果 outHandler 在 inHandler 之后添加，最后一个 inHandler 需要 ctx.channel().write，才能进入 outHandler 的 write
                    // 2.3 如果 out 放在 in 之前，不需要使用 ctx.channel().write
                    //      通常来说，outHandler 都放在前面添加。netty findChannelHandler 机制寻找读事件会先找 out 的 read 方法
                    //      在 in 前面添加的 out 不能在 write 方法内调用 fireChannelRead 时间，否则将 pipeline 会进入死循环
                    // 2.4 out 使用 ctx.write 传递给下一个 out
                    // 2.5 最后一个 in ctx.writeAndFlush 触发给 outHandler，out 最后也需要 ctx.writeAndFlush 才能发送给客户端
                    //      如果多个 in 执行 ctx.writeAndFlush，客户端会接受到多个返回数据，因为 outHandler 会被触发多次
                    // 2.6 客户端先发起请求再接收数据，先 out 后 in，服务端相反
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 出站
                            pipeline.addLast(fieldPrepender);
                            pipeline.addLast(hessianEncode);

                            // 入站
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            pipeline.addLast(hessianDecode);
                            pipeline.addLast(serverHandler);
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("server start port: " + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}