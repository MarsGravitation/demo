package com.microwu.cxd.chinese.hello;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

/**
 * Description: 常见问题
 *  1. Vert.x 机制
 *      Verticle 内部线程安全，除非 Verticle 是 Worker Verticle
 *  2. Verticle 对象和 Handler 的关系
 *      Verticle 对象往往包含一个或多个处理器
 *      Vert.x 保证同一个普通 Verticle(EventLoop Verticle) 内部的所有处理器都只有同一个 EventLoop 线程调用，保证 Verticle 内部线程安全
 *      所以可以在 Verticle 内部声明变量
 *
 *      一个 vert.x 实例有多个 EventLoop 和 Worker 线程，每个线程会部署多个 Verticle 对象并对应执行 Verticle 内的 Handler，
 *      每个 Verticle 内有多个 Handler，普通 Verticle 会跟 EventLoop 绑定，而 Worker Verticle 对象则被 Worker 线程所共享，会依次顺序访问，
 *      当不会并发访问
 *  3. Verticle 之间传递的消息是 immutable 不可变的
 *  4. Vert.x Client
 *      建议客户端和 Verticle 对象绑定，一个 Verticle 对象内保留一个特定的客户端的引用，并在 start 方法中将其实例化，不需要频繁创建
 *      和关闭同类型的客户端
 *      每次使用客户端无需关闭客户端
 *  5. 规避回调地狱
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/20   17:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Faq {

    private Vertx vertx = Vertx.vertx();

    /**
     * 回调地狱
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/20  17:19
     *
     * @param
     * @return  void
     */
    public void test() {
        vertx.eventBus().send("address", "message", messageAsyncResult -> {
            System.out.println(messageAsyncResult.result().body());
        });

        // 使用 future 改造
        Future<Message<String>> future = Future.future();
        // 将回调函数存入 future 中，从而实现代码扁平化
        future.setHandler(messageAsyncResult -> {
            System.out.println(messageAsyncResult.result().body());
        });
        // 用 completer 方法填充
        vertx.eventBus().send("address", "message", future.completer());

    }

    /**
     * 重定向
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/20  17:28
     *
     * @param
     * @return  void
     */
    public void test02() {
        // 本质上是设置响应状态码 302，同时设置响应头 Location 值
//        router.route("/").handler(ctx->ctx.response().putHeader("Location", "/static/index.html").setStatusCode(302).end());
//        router.route("/static/*").handler(StaticHandler.create());
    }

    public static void main(String[] args) {

    }

}