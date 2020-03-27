package com.microwu.cxd.asynchronous.server;

import com.microwu.cxd.asynchronous.callback.Callback3;

/**
 * Description:     服务器对象
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/20   17:17
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Servicer {

    public void response(Callback3 callback3, String request) {
        System.out.println(callback3.getClass().getName() + "进行了请求：" + request);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback3.callback("表单数据");
    }
}