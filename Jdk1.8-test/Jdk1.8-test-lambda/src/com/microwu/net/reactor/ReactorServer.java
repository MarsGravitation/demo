package com.microwu.net.reactor;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/6   16:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReactorServer {
    public static void main(String[] args) throws IOException {
        new Thread(new Reactor(8080)).start();
    }
}