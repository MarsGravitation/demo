package com.microwu.cxd.network.netty.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/8   14:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyPoolClient {

    final NioEventLoopGroup group = new NioEventLoopGroup();
    final Bootstrap bootstrap = new Bootstrap();
    
}