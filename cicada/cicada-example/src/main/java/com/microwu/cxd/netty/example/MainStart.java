package com.microwu.cxd.netty.example;

import com.microwu.cxd.netty.server.CicadaServer;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MainStart {

    public static void main(String[] args) {
        CicadaServer.start(MainStart.class, "/cicada-example");
    }
}