package com.microwu.cxd.server.netty;

import com.microwu.cxd.server.init.CimServerInitializer;
import com.microwu.cxd.server.kit.RegistryZk;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   9:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class CimServer implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CimServer.class);

    private EventLoopGroup boss = new NioEventLoopGroup(1);
    private EventLoopGroup work = new NioEventLoopGroup();

    private int serverPort = 8080;

    @Override
    public void destroy() throws Exception {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                // 保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(new InetSocketAddress(serverPort))
                .childHandler(new CimServerInitializer());

        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            LOGGER.info("start cim server success !!!");

            // 注册 zookeeper
            new Thread(new RegistryZk("127.0.0.1", serverPort)).start();
        }
    }
}