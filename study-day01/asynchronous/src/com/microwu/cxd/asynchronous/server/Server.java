package com.microwu.cxd.asynchronous.server;

import com.microwu.cxd.asynchronous.callback.Callback;

/**
 * Description:     服务器端
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/19   16:00
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Server {
    public void response(Callback callback, String request) {
        System.out.println("服务器端响应码：200");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.callback("请求内容：" + request + "响应结果： data");
    }

}