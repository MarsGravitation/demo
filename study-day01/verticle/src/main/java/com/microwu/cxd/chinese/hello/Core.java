package com.microwu.cxd.chinese.hello;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

import java.util.HashMap;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/20   17:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Core {

    /**
     * 创建实例
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/21  9:21
     *
     * @param
     * @return  void
     */
    public void test() {
        Vertx vertx = Vertx.vertx();

        // 指定配置项
        vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

        // 创建集群模式，因为组成一个集群是需要花费一下时间的，所以异步返回
        VertxOptions options = new VertxOptions();
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                // 获取到集群模式下的 Vertx
                Vertx v = res.result();
                // 做一些其他的事情
            } else {
                // 获取失败，可能是集群管理器出现了问题
            }
        });

        // 流式风格
//        request.response().putHeader("Content-Type", "text/plain").write("some text").end();

        // 大部分 API 是事件驱动的，这意味着当您感兴趣的事情发生时，他会以事件的形势发送给你
        // 你提供处理器给 Vert.x API 来处理事件

        // 没隔一秒发送一个事件的计时器
        vertx.setPeriodic(1000, id -> {
            // 这个处理器将会每隔一秒被调用一次
            System.out.println("timer fired!");
        });

//        server.requestHandler(request -> {
//            // This handler will be called every time an HTTP request is received at the server
//            // 服务器每次收到一个HTTP请求时这个处理器将被调用
//            request.response().end("hello world!");
//        });
        // 当 Vert.x 有一个事件要传给您的处理器时，它会异步的调用这个处理器

        // 不要阻塞

        // Reactor 模式和 Multi-Reactor 模式
        // Vert.x 的 API 都是事件驱动的，当有事件时 Vert.x 会将事件传给处理器来处理
        // 在多数情况下，Vert.x 使用被称为 EventLoop 的线程来调用您的处理器
        // 由于没有阻塞代码，EventLoop 可以在事件到达时快速的分发到不同的处理器中
        // 由于没有阻塞，Event Loop 可以在短时间内分发大量的事件

        // 单一线程的问题在于它在任意时刻只能运行在一个核上
        // Vertx 实例维护的是多个 Event Loop 线程

        // 不要阻塞 EventLoop

        // 运行阻塞式代码
        // 通过调用 executeBlocking 来指定阻塞式代码的执行以及阻塞式执行后处理结果的异步回调
        vertx.executeBlocking(future -> {
            // 调用一些需要耗费显著执行时间返回结果的阻塞式 API
            String result = "";
            future.complete(result);
        }, res -> {
            System.out.println("The result is: " + res.result());
        });

        // 如果 executeBlocking 在同一个上下文环境中（同一个 Verticle 实例）被调用了多次，那么这些不同的 executeBlocking 会顺序执行
        // 一个 Worker Verticle 始终会使用 Worker Pool 中的某个线程来执行

        WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool");
        executor.executeBlocking(future -> {
            // 调用一些需要耗费显著执行时间返回结果的阻塞式 API
        }, res -> {
            System.out.println("The result is: " + res.result());
        });

        // Worker Executor 在不需要的时候必须被关闭
        executor.close();
        // 当使用同一个名字创建了许多 worker 时，它们将共享同一个 pool。当所有的 worker executor 调用了 close 被关闭后，对应的 worker pool 会被销毁
        // 如果 Worker Executor 在 Verticle 中创建，那么 Verticle 实例销毁的同时 Vert.x 将会自动关闭这个 Worker Exector

        // 异步协调
        // 并发合并
        // CompositeFuture.all 接受多个 Future 对象作为参数。当所有的 Future 都成功完成后，返回一个成功 Future，有一个失败时，返回一个失败的 Future
        Future<HttpServer> httpServerFuture = Future.future();
        Future<NetServer> netServerFuture = Future.future();

        CompositeFuture.all(httpServerFuture, netServerFuture).setHandler(ar -> {
            if (ar.succeeded()) {
                // 所有服务器启动成功完成
            } else {
                // 有一个服务器启动失败
            }
        });
        // 所有被合并的 Future 中的操作同时运行。当组合的处理操作完成时，该方法返回的 Future 上绑定的处理器会被调用
        // any 方法的合并会等待第一个成功执行的 Future
        // join 方法的合并会等待所有的 Future 完成，无论成败

        // 顺序合并
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

        // 一个文件被创建、一些东西被写入文件、文件被移走
        //compose: 当前 Future 完成时，执行相关代码，并返回 Future。当返回的 Future 完成时，组合完成
        // compose(handler, next): 当前 Future 完成时，执行相关代码，并完成下一个 Future 的处理
        // 可以使用 completer 方法来串起一个带操作结果的或失败的  Future

    }

    /**
     * Verticle
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/21  10:05
     *
     * @param
     * @return  void
     */
    public void test02() {
        // Verticle 是由 Vert.x 部署和运行的代码块。
        // 默认一个 Vert.x 实例维护了 N（CPU * 2） 个 EventLoop 线程
        // 一个应用程序通常是由在同一个 Vert.x 实例中同时运行的许多 Verticle 实例组合而成。不同 Verticle 实例通过向 EventBus 上发送消息来相互通信

        // standard verticle
        // 当它被创建时，它会被分派给一个 EventLoop，并在这个 EventLoop 中执行他的 start 方法。
        // 当您在一个 EventLoop 上调用了 Core API 中的方法并传入了处理器时，Vert.x 将保证用与调用该方法时相同的 EventLoop 来执行这些处理器
        // 这意味着我们可以保证 Verticle 实例中所欧的代码都是在相同的 EventLoop 中执行

        // Worker Verticle

        // 编程方式部署 Verticle
        // 指定一个 Verticle 名称或者实例来部署
        Vertx vertx = Vertx.vertx();
        MyVerticle myVerticle = new MyVerticle();
        vertx.deployVerticle(myVerticle, res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
            }
        });

        // 撤销 Verticle
        vertx.undeploy("", res -> {
            if (res.succeeded()) {
                System.out.println("Undeployed ok");
            } else {
                System.out.println("Undeploy failed!");
            }
        });

        // Context 对象
        // 当 Vert.x 传递一个时间给处理器或者调用 Verticle 的 start/stop 方法时，它会关联一个 Context
        // 对象来执行。通常来说这个 Context 会是一个 EventLoopContext，它绑定到了一个特定的 EventLoop 线程上，
        // 所以在该 Context 上执行的操作总是在同一个 EventLoop
        Context context = vertx.getOrCreateContext();
        // 同一个 Context 运行了多个处理函数，需要共享数据
        context.put("data", "hello");
        context.runOnContext(v -> {
            // 提交的任务将会在同一个 Context 中运行
            System.out.println("This will be executed asynchronously in the same context");
        });

        // 执行周期性/延迟性操作
        // 一次性计数器
        // 返回值是一个唯一的计数器 id，可用于取消该计时器
        long timerId = vertx.setTimer(1000, id -> {
            System.out.println("And one second later this is printed");
        });
        // 周期性计时器
        timerId = vertx.setPeriodic(1000, id -> {
            System.out.println("And every second this is printed");
        });
        // 取消计时器
        vertx.cancelTimer(timerId);

        // json 对象
        String jsonString = "{\"foo\":\"bar\"}";
        // 字符串创建
        JsonObject object = new JsonObject(jsonString);
        // map 创建
        object = new JsonObject(new HashMap<>());
        // 将键值对放入 json 对象
        object.put("foo", "bar");
        // 从 json 对象中获取值
        String foo = object.getString("foo");
        // json 对象和 Java 对象间的映射
        User user = object.mapTo(User.class);
        // 将 json 对象编码成字符串
        String encode = object.encode();

        // json 数组
       // 使用字符串创建
        jsonString = "[\"foo\",\"bar\"]";
        JsonArray array = new JsonArray(jsonString);
        // 将数组项添加到 json 数组中
        array.add("foo");
        // 获取值
        String string = array.getString(0);
        // json 数组编码成字符串
        array.encode();

        // Buffer
        // 在 Vert.x 内部，大部分数据被重新组织成 buffer 格式
        // 一个 Buffer 是可以读取或者写入的 0 或多个字节序列，并且根据需要可以自动扩容，将任意字节写入 Buffer
        Buffer buffer = Buffer.buffer();
        Buffer.buffer("some string");
        Buffer.buffer(new byte[]{});
        Buffer.buffer(1000);
        // 写数据
        // 追加
        buffer.appendInt(123).appendString("a");
        // 随机访问写 buffer
        buffer.setInt(100, 123);
        // 读取数据
        for (int i = 0; i < buffer.length(); i+=4) {
            System.out.println(buffer.getInt(i));
        }

        // 配置 TCP 服务端
        NetServerOptions options = new NetServerOptions().setPort(4321);
        // 编写 TCP 服务端和客户端
        NetServer server = vertx.createNetServer(options);
        // 启动服务端监听
        server.listen(res -> {
            if (res.succeeded()) {
                System.out.println("Server is now listening!");
            } else {
                System.out.println("Failed to bind!");
            }
        });
        // 接受传入连接时的通知
        server.connectHandler(socket -> {
            // 这里处理传入连接
            // 连接成功时，可以在回调函数中处理得到的 socket 实例，
            // 这是代表了实际连接的套接字连接，他允许读取和写入数据
            socket.handler(buff -> {
                // 从 socket 读取数据
                // socket 调用 handler 来读取数据
                // 每次 socket 接收到数据后，会以 buffer 对象为参数调用处理器
                System.out.println("I received some bytes: " + buffer.length());

                // 写操作
                Buffer b = Buffer.buffer().appendString("a");
                socket.write(b);
            });
        });

    }

    /**
     * 编写 Http 服务端和客户端
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/21  11:02
     *
     * @param
     * @return  void
     */
    public void test03() {
        // 配置
        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(1000000);
        // 创建 HTTP 服务端
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer(options);
        // 开启服务端监听
        server.listen(8080,"www.baidu.com", res -> {
            if (res.succeeded()) {
                System.out.println("Server is now listening!");
            } else {
                System.out.println("Failed to bind!");
            }
        });

        // 收到传入请求的通知
        // 当请求到达时，Vert.x 会向对应的处理函数传入一个 request 实例并调用请求处理函数
        server.requestHandler(request -> {
            // 当请求的头信息被完全读取时会调用该请求处理器
            // 如果请求包含请求体，那么该请求体将在请求处理器被调用后的某个时间到达服务器
            // 请求对象允许检索 uri，path，params，headers
            // 每个请求对象和一个响应对象绑定

            // 请求体中读取数据
            // 请求头部到达时，请求处理器会被调用，请求对象中没有请求体
            // 因为请求体可能非常大，调用 handler 设置处理器，每次请求体的一小块数据收到时，该处理器都会被调用
            // 可以进行聚合
            // 或者 bodyHandler 进行处理
            request.handler(buffer -> {
                System.out.println("I have received a chunk of the body of length " + buffer.length());
            });
            request.response().end("Hello world");

            // 读取表单数据
            // urlencoded 可以如同普通查询参数一样
            // multipart 必须在整个请求体完全读取才可用
            // 先设置 Multipart 方法
            request.setExpectMultipart(true);
            request.endHandler(v -> {
                // 请求体被完全读取，所以直接读取表单属性
                MultiMap entries = request.formAttributes();
            });

            // 文件上传
            request.uploadHandler(upload -> {
                System.out.println("Got a file upload " + upload.name());
                // 分块上传
                upload.handler(chunk -> {
                    System.out.println("Received a chunk of the upload of length " + chunk.length());
                    // 保存到服务器磁盘上
                    upload.streamToFileSystem("/directory/" + upload.filename());
                });
            });

            // 发回响应
            // 设置响应码和消息
            HttpServerResponse response = request.response();
            response.setStatusCode(200);
            response.setStatusMessage("Hello World");
            // 写入数据
            response.write(Buffer.buffer());
            response.write("hello world!");
            response.write("hello world!", "UTF-16");
            // 完成响应
            response.end();
            // 设置响应头
            MultiMap headers = response.headers();
            headers.set("content-type", "text/html");
            response.putHeader("other-header", "wibble");
        });

    }

    class MyVerticle extends AbstractVerticle {
        @Override
        public void start() throws Exception {
            // 部署时调用
            super.start();
        }

        @Override
        public void start(Future<Void> startFuture) throws Exception {
            // 异步 start
            // 方法执行完后，Verticle 实例并没有部署好
            // 调用 Future 的 complete/fail 来标记启动的完成或者失败
            vertx.deployVerticle("com.foo.OtherVerticle", res -> {
                if (res.succeeded()) {
                    startFuture.complete();
                } else {
                    startFuture.fail(res.cause());
                }
            });
        }

        @Override
        public void stop() throws Exception {
            // 撤销时调用
            super.stop();
        }
    }

    class User {

    }

}