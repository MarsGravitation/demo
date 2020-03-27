package com.microwu.cxd.asynchronous.test;

import com.microwu.cxd.asynchronous.client.Browser;
import com.microwu.cxd.asynchronous.server.Servicer;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/20   17:30
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CallbackTest3 {
    public static void main(String[] args) {
        new Browser(new Servicer()).request("请求表单数据");
    }
}