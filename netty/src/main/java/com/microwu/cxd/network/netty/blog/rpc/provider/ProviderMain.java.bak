package com.microwu.cxd.network.netty.blog.rpc.provider;

import com.microwu.cxd.network.netty.blog.rpc.client.QueryStudentClient;
import com.microwu.cxd.network.netty.blog.rpc.kernel.netty.server.NettyServer;
import com.microwu.cxd.network.netty.blog.rpc.kernel.registry.Registry;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   14:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ProviderMain {
    public static void main(String[] args) throws InterruptedException {
        Registry.map.put(QueryStudentClient.class.getName(), QueryStudentClientImpl.class);

        NettyServer server = new NettyServer();
        server.bind(8080);
    }
}