package com.microwu.cxd.chinese.hello;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;

/**
 * Description: 常见问题
 * 1. Vert.x 机制
 * Verticle 内部线程安全，除非 Verticle 是 Worker Verticle
 * 2. Verticle 对象和 Handler 的关系
 * Verticle 对象往往包含一个或多个处理器
 * Vert.x 保证同一个普通 Verticle(EventLoop Verticle) 内部的所有处理器都只有同一个 EventLoop 线程调用，保证 Verticle 内部线程安全
 * 所以可以在 Verticle 内部声明变量
 * <p>
 * 一个 vert.x 实例有多个 EventLoop 和 Worker 线程，每个线程会部署多个 Verticle 对象并对应执行 Verticle 内的 Handler，
 * 每个 Verticle 内有多个 Handler，普通 Verticle 会跟 EventLoop 绑定，而 Worker Verticle 对象则被 Worker 线程所共享，会依次顺序访问，
 * 当不会并发访问
 * 3. Verticle 之间传递的消息是 immutable 不可变的
 * 4. Vert.x Client
 * 建议客户端和 Verticle 对象绑定，一个 Verticle 对象内保留一个特定的客户端的引用，并在 start 方法中将其实例化，不需要频繁创建
 * 和关闭同类型的客户端
 * 每次使用客户端无需关闭客户端
 * 5. 规避回调地狱
 *  https://www.null123.com/article/detail-139161.html
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
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2021/1/20  17:19
     */
    public void test() {
        // Future 对象提供了一种异步结果的包装，用户可使用 Future 类中的 setHandler 方法
        // 来保证回调函数，然后在原先使用该回调函数的地方用 completer 方法予以填充，这样便
        // 可将回调函数从原参数中取出，以减少回调缩进，从而规避回调地狱

        // 未使用 future 时，回调函数嵌在 send 方法内部，以匿名函数的形式作为 send 的参数
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

        // 一个复杂一点的例子

        // 以下程序先向 address1 发送一个 message，然后等 address1 回复之后，将
        // address1 的回复消息发送给 address2，最后将 address2 的回复
        // 打印到控制台上

        vertx.eventBus().send("address1", "message", asyncResult -> {
            vertx.eventBus().send("address2", asyncResult.result().body(), asyncResult2 -> {
                System.out.println(asyncResult2.result().body());
            });
        });

        // 通过以下方式解决代码过多缩进的问题
        Future<Message<String>> future1 = Future.future();
        Future<Message<String>> future2 = Future.future();

        future1.setHandler(asyncResult -> {
            vertx.eventBus().send("address2", asyncResult.result().body(), future2);
        });

        future2.setHandler(asyncResult -> {
            System.out.println(asyncResult.result().body());
        });

        vertx.eventBus().send("address1", "message", future1);

        // 使用 Future 来包装异步代码块 (3.4.0+)
        // 早期版本的 Future，针对每一个异步的过程，需要在代码中声明中间变量 future 来对应
        // 异步回调
        // 如：Future<Message<String>> future1 = Future.future();

        // 在最新的版本中(3.4.0+)，可以通过 Future.future 方法以函数式的方式直接用 Future 来
        // 包装一个异步的代码块

        // 该方法的输入参数是一个 Function，该 Function 会以一个新的 Future 实例为参数
        // 被调用。由于 Future 自身实现了 Handler<AsyncResult>，因此你可以将它直接作为
        // 回调的 Handler 传入到异步方法里。该方法的返回值是提供给异步调用使用的
        // Future 实例。因此可以避免为嵌套的多个异步操作定义不同的 Future 变量，使代码更
        // 为简洁。
        Future.<Message<String>>future((future3) -> {
            vertx.eventBus().send("address1", "someMessage", future3);
        });

        // 以下两种写法是等效的
        vertx.eventBus().send("address", "message", future.completer());
        vertx.eventBus().send("address", "message", future);

        // 复杂一点的例子
        future.compose(message -> Future.<Message<String>>future(f ->
                        vertx.eventBus().send("address", message.body(), f)
                )
        );

        future.compose(message -> {
            Future<Message<String>> f = Future.future();
            vertx.eventBus().send("address", message.body(), f);
            return f;
        });

        // 使用组合来实现链式调用 (3.4.0+)
        // Future 接口提供了 compose 方法来链式地组合多个异步操作。

        try {
            String msg1 = send("address1", "message");
            String msg2 = send("address2", msg1);
            String msg3 = send("address3", msg2);
            // deal with result
        } catch (Exception e) {
            // deal with exception
        }

        // 通过 compose 替换
        // 每一个 compose 方法需要传入一个 Function，Function 的输入是前一个异步过程
        // 的返回值（此处的返回值不是 AsyncResult，而是具体的返回内容），需要返回一个新
        // 的需要链接的 Future。该 Function 方法当且仅当前一个异步流程执行成功时才会被
        // 调用
        Future.<Message<String>>future(f -> {
                    vertx.eventBus().send("address1", "message", f);
        }
        ).compose(msg ->
                Future.<Message<String>>future(f ->
                        vertx.eventBus().send("address2", msg.body(), f)
                )
        ).compose(msg ->
                Future.<Message<String>>future(f ->
                        vertx.eventBus().send("address3", msg.body(), f)
                )
        ).setHandler(res -> {
            if (res.failed()) {
                // deal with exception
                return;
            }
            // deal with the result
        });


        FileSystem fs = vertx.fileSystem();
        Future<Void> startFuture = Future.future();

        Future<Void> fut1 = Future.future();
        fs.createFile("/foo", fut1.completer());

        fut1.compose(v -> {
            // fut1中文件创建完成后执行
            Future<Void> fut2 = Future.future();
            fs.writeFile("/foo", Buffer.buffer(), fut2.completer());
            return fut2;
        }).compose(v -> {
                    // fut2文件写入完成后执行
                    fs.move("/foo", "/bar", startFuture.completer());
                },
                // 如果任何一步失败，将startFuture标记成failed
                startFuture);
    }

    private String send(String address1, String message) {
        return address1 + message;
    }

    /**
     * 重定向
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2021/1/20  17:28
     */
    public void test02() {
        // 本质上是设置响应状态码 302，同时设置响应头 Location 值
//        router.route("/").handler(ctx->ctx.response().putHeader("Location", "/static/index.html").setStatusCode(302).end());
//        router.route("/static/*").handler(StaticHandler.create());
    }

    public static void main(String[] args) {

    }

}