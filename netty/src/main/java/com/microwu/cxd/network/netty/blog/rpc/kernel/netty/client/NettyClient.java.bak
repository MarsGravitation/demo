package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.client;

import com.microwu.cxd.network.netty.blog.rpc.kernel.remote.RpcContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyClient {

    private String host;
    private int port;

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private ChannelFuture future;
    private NettyClientHandlerInitializer clientHandlerInitializer;
    // 一般设置为线程的数量，当一个线程执行完毕后，await 的线程就可以继续运行
    private CountDownLatch latch;

    public NettyClient(String host, int port) {
        this.port = port;
        this.host = host;

        latch = new CountDownLatch(0);
        clientHandlerInitializer = new NettyClientHandlerInitializer(latch);
        group = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(clientHandlerInitializer);
    }

    public void connect() {
        try {
            this.future = bootstrap.connect(host, port).sync();
            System.out.println("远程服务器已经连接，可以进行数据交换。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ChannelFuture getChannelFuture() {
        if (this.future == null) {
            this.connect();
        }
        if (!this.future.channel().isActive()) {
            this.connect();
        }
        return this.future;
    }

    public void close() {
        try {
            this.future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端发送数据方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/7  15:40
     *
     * @param   	rpcContext
     * @return  java.lang.Object
     */
    public Object sendData(RpcContext rpcContext) throws InterruptedException {
        // 单例模式获取 ChannelFuture 对象
        ChannelFuture future = this.getChannelFuture();
        if (future.channel() != null && future.channel().isActive()) {
            latch = new CountDownLatch(1);
            clientHandlerInitializer.reLatch(latch);
            future.channel().writeAndFlush(rpcContext);
            latch.await();
        }
        return clientHandlerInitializer.getServerResult();
    }

}