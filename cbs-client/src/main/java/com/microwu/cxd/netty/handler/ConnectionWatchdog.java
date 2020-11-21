package com.microwu.cxd.netty.handler;

import com.microwu.cxd.netty.core.Address;
import com.microwu.cxd.netty.core.Connection;
import com.microwu.cxd.netty.utils.TimerHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.Timer;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/2   18:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ChannelHandler.Sharable
public class ConnectionWatchdog extends ChannelInboundHandlerAdapter {

    private static final Timer timer = TimerHolder.getInstance();

    private static final int BACK_OFF_CAP = 12;

    private final Bootstrap bootstrap;

    public ConnectionWatchdog(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = Connection.getConnection(ctx.channel());
        if (!connection.isFine()) {
            reconnect(connection, 1);
        }
        super.channelInactive(ctx);
    }

    private void reconnect(final Connection connection, final int attempts) {
        System.out.println("重连。。。");
        int timeout = 1 << attempts;
        timer.newTimeout(t -> {
            Address address = connection.getAddress();
            InetSocketAddress socketAddress = new InetSocketAddress(address.getIp(), address.getPort());
            this.bootstrap.connect(socketAddress).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        connection.updateChannel(channelFuture.channel());
                        return;
                    }
                    reconnect(connection, Math.min(BACK_OFF_CAP, attempts + 1));
                }
            });
        }, timeout, TimeUnit.SECONDS);
    }
}