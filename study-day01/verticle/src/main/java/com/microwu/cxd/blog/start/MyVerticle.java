package com.microwu.cxd.blog.start;

import io.vertx.core.AbstractVerticle;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/3   16:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("MyVerticle started!");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("MyVerticle stop!");
    }
}