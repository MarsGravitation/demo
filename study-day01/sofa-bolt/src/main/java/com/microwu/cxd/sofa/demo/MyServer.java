package com.microwu.cxd.sofa.demo;

import com.alipay.remoting.rpc.RpcServer;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/24   9:37
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyServer {

    public static boolean start() {
        RpcServer server = new RpcServer(8888);
        server.registerUserProcessor(new MyServerUserProcessor());

        return server.start();
    }

    public static void main(String[] args) {
        if (MyServer.start()) {
            System.out.println("server start success!");
        } else {
            System.out.println("server start fail!");
        }
    }
}