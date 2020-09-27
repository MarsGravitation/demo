package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CountDownLatch;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyClientHandler extends ChannelHandlerAdapter {
    private CountDownLatch latch;
    private Object result;

    public NettyClientHandler(CountDownLatch latch) {
        this.latch = latch;
    }

    public Object getResult() {
        return result;
    }

    public void reLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        result = msg;
        System.out.println("返回数据读取完毕");
        latch.countDown();
    }
}