package com.microwu.cxd.netty.core;

import com.microwu.cxd.netty.handler.*;
import com.microwu.cxd.netty.utils.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   12:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DefaultClientConnectionFactory implements ConnectionFactory {

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1, new NamedThreadFactory("cbs-netty-client-worker", true));

    private Bootstrap bootstrap;

    private final EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(16);

    @Override
    public void init() {
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, false)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("decoder", new MessageDecoder(1024 * 1024, 0, 4, -4, 0))
                                .addLast("encoder", new MessageEncoder())
                                .addLast("idleHandler", new IdleStateHandler(15, 15, 15, TimeUnit.SECONDS))
                                .addLast("heartbeatHandler", new HeartbeatHandler())
                                .addLast("connectionWatchdog", new ConnectionWatchdog(bootstrap))
                                // 链路检测
                                .addLast(new EnquireLinkReqHandler())
                                // 上传元素鉴权反馈
                                .addLast(businessGroup, new ChatbotMultiRspHandler())
                                // MaaP 业务富媒体卡片消息鉴权反馈
                                .addLast(businessGroup, new RichcardRspHandler())
                                // 等待人工审核结果通知Maap服务接口
                                .addLast(businessGroup, new ResultNotifyRspHandler());
                    }
                });
    }

    @Override
    public Connection create(Address address) {
        String ip = address.getIp();
        int port = address.getPort();
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
        System.out.println(System.currentTimeMillis() / 1000 + ": 连接成功 。。。");

        future.awaitUninterruptibly();

        Channel channel = future.channel();

        Connection connection = new Connection(channel, address);
        return connection;
    }
}