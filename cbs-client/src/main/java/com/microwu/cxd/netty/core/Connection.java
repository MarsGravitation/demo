package com.microwu.cxd.netty.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description: 连接元实例，包含了 Netty Channel
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   8:25
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Connection {

    private Channel channel;

    private Address address;

    private AtomicBoolean closed = new AtomicBoolean(false);

    public static final AttributeKey<Connection> CONNECTION = AttributeKey.valueOf("connection");

    public static final AttributeKey<Integer> HEARTBEAT_COUNT = AttributeKey.valueOf("heartbeatCount");

    public Connection(Channel channel, Address address) {
        this.channel = channel;
        this.address = address;
        this.channel.attr(CONNECTION).set(this);
        init();
    }

    private void init() {
        this.channel.attr(HEARTBEAT_COUNT).set(0);
    }

    public Address getAddress() {
        return this.address;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public static Connection getConnection(Channel channel) {
        return channel.attr(CONNECTION).get();
    }

    /**
     * 检查连接是否良好
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/29  18:01
     *
     * @param   	
     * @return  boolean
     */
    public boolean isFine() {
        return this.channel != null && this.channel.isActive();
    }

    /**
     * 关闭连接
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/29  22:10
     *
     * @param
     * @return  void
     */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            if (this.channel != null) {
                this.channel.closeFuture().addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println("关闭连接。。。");
                    }
                });
            }
        }
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
        this.channel.attr(CONNECTION).set(this);
        init();
    }
}