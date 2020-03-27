package com.microwu.cxd.asynchronous.client;

import com.microwu.cxd.asynchronous.callback.Callback3;
import com.microwu.cxd.asynchronous.server.Servicer;

/**
 * Description:    浏览器对象
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/20   17:16
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Browser {
    private Servicer servicer;


    public Browser(Servicer servicer) {
        this.servicer = servicer;
    }

    public void request(final String request) {
        System.out.println("发送ajax请求。。。");
        // 因为那边需要查询数据库，可能会产生阻塞，等待时间比较长，
        // 浏览器这边启动一个线程，让这个线程等待，主线程继续执行其他任务
        new Thread(() -> {
            // 服务器需要知道是谁给它发送的请求
            // 服务器需要知道请求内容
            servicer.response(new MyCallback(), request);
        }).start();
        down();
    }

    public void down() {
        System.out.println("向下执行。。。");
    }

    private class MyCallback implements Callback3 {

        @Override
        public void callback(String data) {
            System.out.println("处理完毕，返回数据：" + data);
        }
    }
}