package com.microwu.cxd.asynchronous.client;

import com.microwu.cxd.asynchronous.callback.Callback;
import com.microwu.cxd.asynchronous.server.Server;

/**
 * Description:     客户端
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/19   16:00
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Client {
    private Server server;

    public Client(Server server) {
        this.server = server;
    }

    public void request() {
        System.out.println("发送ajax请求。。。。");
        new Thread(() -> {
            server.response(new MyCallback(), "查询数据库。。。");
        }).start();
        show();
    }

    public void show() {
        System.out.println("展示其他。。。");
    }

    private class MyCallback implements Callback {

        @Override
        public void callback(String message) {
            System.out.println(message);
        }
    }
}