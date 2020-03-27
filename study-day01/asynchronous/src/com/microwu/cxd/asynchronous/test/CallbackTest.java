package com.microwu.cxd.asynchronous.test;

import com.microwu.cxd.asynchronous.client.Client;
import com.microwu.cxd.asynchronous.server.Server;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/19   16:14
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CallbackTest {
    public static void main(String[] args) {
        new Client(new Server()).request();
    }
}