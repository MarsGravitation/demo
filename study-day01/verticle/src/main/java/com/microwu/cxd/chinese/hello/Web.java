package com.microwu.cxd.chinese.hello;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/21   11:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Web {

    public void test() {
        HttpServer server = Vertx.vertx().createHttpServer();
        server.requestHandler(request -> {
            // 所有的请求的都会调用这个处理器处理
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");

            // 写入响应并结束
            response.end("Hello World!");
        });
        server.listen(8080);
    }

    public void test02() {
        // Router 是 Web 的核心概念之一，它维护了零或多个 Route 对象
        // Router接受　HTTP 请求，并查找首个匹配该请求的 Route，然后将请求传递给这个 Route

        // Route 可以持有一个与之关联的处理器用于接受请求。可以通过这个处理器对请求做一些事情
        // 然后结束响应或者吧请求传递给下一个匹配的处理器
        Vertx vertx = Vertx.vertx();
        // 创建一个 Http 服务器
        HttpServer server = vertx.createHttpServer();
        // 创建 Router
        Router router = Router.router(vertx);
        // 创建了一个没有匹配条件的 route，这个 route 会匹配所有到达这个服务器的请求
        // 为 route 指定了一个处理器，所有的请求都会调用这个处理器处理
        router.route().handler(routingContext -> {
           // 所有的请求都会调用这个处理器处理
            // RoutingContext 包含了 request 和 response，还有各种简化的东西
            // 每一个被路由的请求对应一个唯一的 RoutingContext，这个实例会被传送到所有处理这个请求的处理器上
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // 写入响应并结束处理
            response.end("Hello World from Vert.x-Web!");
        });

        // 设置 Http 服务器的请求处理器，使所有的请求都通过 accept 接受
        server.requestHandler(router::accept).listen(8080);

        // 处理请求并调用下一个处理器

        // Web 决定路由一个请求到匹配的 route 上，它会使用一个 RoutingContext 调用对应处理器。
        // 如果不在处理器结束这个响应，调用 next 方法让其他匹配 Route 来处理请求
        Route route = router.route("/some/path/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            // 由于我们会在不同的处理器里写入响应，因此需要启用分块传输
            response.setChunked(true);
            response.write("route1\n");

            // 5 秒后调用下一个处理器
            routingContext.vertx().setTimer(5000, tid -> {
                routingContext.next();
            });
        });

        Route route2 = router.route("/some/path/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.write("route2\n");

            routingContext.vertx().setTimer(5000, tid -> {
                routingContext.next();
            });
        });

        Route route3 = router.route("/some/path/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.write("route3");

            // 结束响应
            routingContext.response().end();
        });

        // route1 向响应里写了数据，5 秒后 route2 向响应里写入了数据，再 5 秒 后 route3 向响应里写入了数据并结束了响应
        // 所有发生的这些没有线程阻塞

        // 使用阻塞式处理器
        router.route().blockingHandler(routingContenxt -> {
            // 执行某些同步的耗时操作
            // 调用写一个处理器
            routingContenxt.next();
        });

        // 默认情况下在一个 context（core 的 context）上执行的所有阻塞式处理器的执行是顺序的，也就意味着只有一个处理器执行完了才会继续执行下一个
        router.post("/some/endpoint").handler(ctx -> {
            // 阻塞处理器中处理一个 multipart 类型的表单数据
            // 首先调用下面的方法
            ctx.request().setExpectMultipart(true);
            ctx.next();
        }).blockingHandler(ctx -> {
            // 执行某些阻塞操作
        });

        // 基于精确路径的路由
        // 只匹配指定的 URI，只会匹配路径一致的请求
        // 结尾的 / 会被忽略
        Route route4 = router.route().path("/some/path/");

        // 基于路径前缀的路由
        // 匹配以 /some/path 开头的请求
        router.route().path("/some/path/*");

        // 捕获路径参数
        // 通过占位符声明路径参数并在处理请求时通过 params 方法获取
        // 占位符由 ：和参数名组成
        router.route(HttpMethod.POST, "/catelogue/productes/:producttype/:productid/");

        // 基于正则表达式的路由
        router.route().pathRegex(".*foo");
        // 基于 method 的路由
        router.route().method(HttpMethod.POST);

        // 路由顺序
        // 默认的路由的匹配顺序与添加到 router 的顺序一致
        // 当一个请求达到时，router 会一步一步检查每一个 route 是否匹配，如果匹配则对应的处理器会被调用
        // 如果处理器随后调用了 next，下一个匹配的 route 对应的处理器会被调用
        // 上面那个例子，route -> route2 -> route3
        // 可以覆盖路由的默认顺序

        // 基于请求媒体类型的路由
        router.route().consumes("text/html");

        // 组合路由规则
        // 这会匹配所有路径以 `/myapi/orders` 开头，`content-type` 值为 `application/json` 并且 `accept` 值为 `application/json` 的 PUT 请求
        router.route(HttpMethod.PUT, "myapi/orders")
                .consumes("application/json")
                .produces("application/json");

        // 上下文数据
        // 通过路由上下文维护处理器共享的数据
        router.route().handler(rc -> {
           rc.put("foo", "bar");
           rc.get("foo");
        });

        // 重定向
        router.get("/my-pretty-notfound-handler").handler(ctx -> {
            ctx.response()
                    .setStatusCode(404)
                    .end("NOT FOUND fancy html here!!!");
        });

        router.get().failureHandler(ctx -> {
            if (ctx.statusCode() == 404) {
                ctx.reroute("/my-pretty-notfound-handler");
            } else {
                ctx.next();
            }
        });

        // 子路由
        // /products/product1234 GET/PUT/DELETE 都会调用这个 API
        Router restAPI = Router.router(vertx);
        restAPI.get("/products/:productID").handler(rc -> {

        });
        restAPI.put("/products/:productID").handler(rc -> {

        });
        restAPI.delete("/products/:productID").handler(rc -> {

        });

        // 默认 404 处理器
        // 如果没有为请求匹配到任何路由， web 会声明一个 404 错误
        // 这可以被自己实现的处理器处理，或者专用错误处理器处理，如果没有提供错误处理器，会发送一个基本的 404 响应

        // 错误处理
        router.get("/somepath/*").failureHandler(frc -> {
            // 如果发送错误，会调用这个处理器
        });

        // 处理请求消息体
        // 使用消息体处理器 BodyHandler 来获取请求的消息体，限制消息体大小或者处理文件上传
        // 需要保证消息体处理器能够匹配所有的功能请求
        // 由于它需要在所有异步执行之前处理请求的请求体，因此这个处理器要尽可能早地设置到 router 上
        router.route().handler(BodyHandler.create());
        // 获取请求的消息体
        // json：getBodyAsJson
        // String: getBodyAsString
        // Buffer: getBody

        // 限制消息体大小
        // setBodyLimit

        // 合并表单属性 - 默认合并表单属性到请求的参数里
        // 处理文件上传
        // 当消息体处理器匹配到请求时，所有上传的文件会被自动写入上传目录，默认为 file-uploads
        // 每一个上传的文件会被自动生成一个文件名，并可以通过 rc 的 fileUploads 获得
        router.route().handler(BodyHandler.create());
        router.post("/some/path/uploads").handler(rc -> {
            // 每个上传的文件通过一个 FileUpload 描述
            Set<FileUpload> uploads = rc.fileUploads();
            // 执行上传处理
        });
    }

    public static void main(String[] args) {

    }

}