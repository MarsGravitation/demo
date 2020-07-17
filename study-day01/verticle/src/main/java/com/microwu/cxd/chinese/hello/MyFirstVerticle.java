package com.microwu.cxd.chinese.hello;

import io.vertx.core.AbstractVerticle;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/3   17:10
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyFirstVerticle extends AbstractVerticle {

    /**
     * Verticle 对象中包含了一个或者多个处理器对象
     * 在 Vert.x 中，完成 Verticle 的部署之后，真正调用处理逻辑的入口往往是处理器，
     * Vert.x 保证同一个普通 Verticle，也就是 EventLoop Verticle 内部的所有处理器都只会由
     * 同一个 EventLoop 线程调用，由此保证 Verticle 内部线程安全，所以我们可以放心的在
     * Verticle 内部声明各种线程不安全的属性变量，并在 handler 中分享它们
     *
     * 一个vert.x 实例/进程内有多个 EventLoop 和 Worker 线程，每个线程会部署多个 Verticle 对象
     * 并对应执行 Verticle 内的 Handler，每个 Verticle 内有多个 Handler，普通Verticle 会跟
     * EventLoop 绑定，而 Worker Verticle 对象则会被 Worker 线程所共享，会依次顺序访问，
     * 但并不会并发同时访问。
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/3  17:19
     *
     * @param
     * @return  void
     */
    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(httpServerRequest -> {
            httpServerRequest.response()
                    .putHeader("content-type", "text/plain")
                    // 要关闭请求，否则连接很快就会被占满
                    .end("Hello World!");
        }).listen(8080);
    }
}