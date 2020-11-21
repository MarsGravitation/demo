package com.microwu.cxd.netty.client;

import com.microwu.cxd.netty.core.Address;
import com.microwu.cxd.netty.core.Connection;
import com.microwu.cxd.netty.core.ConnectionManager;
import com.microwu.cxd.netty.struct.BcnMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.ExecutionException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   11:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CbsClientRemoting {

    private final ConnectionManager connectionManager;

    public CbsClientRemoting(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void oneWay(Address address, BcnMessage message) {
        try {
            Connection connection = this.connectionManager.getAndCreateIfAbsent(address);
            this.connectionManager.check(connection);
            connection.getChannel().writeAndFlush(message).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        System.out.println("oneWay request fail ...");
                    } else {
                        System.out.println("send message success ...");
                    }
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}