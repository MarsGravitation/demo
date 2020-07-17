//package com.microwu.cxd.blog.web;
//
//import io.vertx.core.AbstractVerticle;
//import io.vertx.core.eventbus.EventBus;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.Router;
//import io.vertx.ext.web.RoutingContext;
//import io.vertx.ext.web.handler.BodyHandler;
//import io.vertx.ext.web.handler.StaticHandler;
//
///**
// * Description:
// *  HttpServer: vert.x 发布 restful 服务，它自己就是一台性能强大的 web 服务器，并且原生支持负载均衡
// *  Router：可以理解为一个路由器，接受 HttpServer 带来的请求，并将不同的请求分发到不同的路由器，可以将 router 理解为 dispatcher
// *  route： 代表一条路由，相当于 @RequestMapping，它指定了 restful api 的请求接口路径，并将其交给 handler 来处理该条路由
// *  Handler： 处理具体请求的路由，字面上就讲的就是处理某个具体的restful api
// *      来自 HttpServer 的 request 请求 -> 交给路由器做分发处理 -> 路由器匹配到具体的路由规则 -> 路由到最终的 handler 去处理请求
// *      默认的处理器： AuthHandler（权限）、BodyHandler（所有请求上下文）、CookieHandler、SessionHandler
// *  RoutingContext：请求上下文，可以理解为 servlet 中的 http request 和 http response
// *   router - 定义请求的分发
// *   handlers - 这是实际处理请求并且返回结果的地方，handler 可以被链接起来使用
// *
// *  router 负责分发 HTTP 请求到 handler
// *
// *
// *  http://www.360doc.com/content/18/0203/14/39530679_727432611.shtml
// *
// * @Author: chengxudong             chengxudong@microwu.com
// * Date:       2020/7/3   16:08
// * Copyright:      北京小悟科技有限公司       http://www.microwu.com
// * Update History:
// * Author        Time            Content
// */
//public class RestfulVerticle extends AbstractVerticle {
//
//    @Override
//    public void start() {
//        // 实例化一个路由器出来，用来路由不同的rest接口
//        Router router = Router.router(vertx);
//        // 增加一个处理器，将请求的上下文信息，放到RoutingContext中
//        router.route().handler(BodyHandler.create());
//
//        // 处理一个post方法的rest接口
//        router.post("/post/:param1/:param2").handler(this::handlePost);
//        // 处理一个get方法的rest接口
//        router.get("/get/:param1/:param2").handler(this::handleGet);
//        router.route("/assets/*").handler(StaticHandler.create("assets"));
//        // 创建一个httpserver，监听8080端口，并交由路由器分发处理用户请求
//        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
//
//    }
//
//    // 处理post请求的handler
//    private void handlePost(RoutingContext context) {
//        // 从上下文获取请求参数，类似于从httprequest中获取parameter一样
//        String param1 = context.request().getParam("param1");
//        String param2 = context.request().getParam("param2");
//
//        if (isBlank(param1) || isBlank(param2)) {
//            // 如果参数空，交由httpserver提供默认的400错误界面
//            context.response().setStatusCode(400).end();
//        }
//
//        JsonObject obj = new JsonObject();
//        obj.put("method", "post").put("param1", param1).put("param2", param2);
//
//        // 申明response类型为json格式，结束response并且输出json字符串
//        context.response().putHeader("content-type", "application/json")
//                .end(obj.encodePrettily());
//    }
//
//    private void handleGet(RoutingContext context) {
//        // 从上下文获取请求参数，类似于从httprequest中获取parameter一样
//        String param1 = context.request().getParam("param1");
//        String param2 = context.request().getParam("param2");
//
//        // 如果参数空，交由httpserver提供默认的400错误界面
//        if (isBlank(param1) || isBlank(param2)) {
//            context.response().setStatusCode(400).end();
//        }
//        JsonObject obj = new JsonObject();
//        obj.put("method", "get").put("param1", param1).put("param2", param2);
//
//        context.response().putHeader("content-type", "application/json")
//                .end(obj.encodePrettily());
//    }
//
//    private boolean isBlank(String str) {
//        if (str == null || "".equals(str)) {
//            return true;
//        }
//        return false;
//    }
//
//
//    public void sendMeg(String param2) {
//        EventBus eb = vertx.eventBus();
//        eb.send("news.uk.sport", "2---Yay! Someone kicked a ball across a patch of grass", ar -> {
//            if (ar.succeeded()) {
//                System.out.println("Received reply: " + ar.result().body());
//            }
//        });
//    }
//
//
//    public void publishMeg(String param2) {
//        EventBus eb = vertx.eventBus();
//        eb.publish("news.uk.sport", "2---Yay! Someone kicked a ball across a patch of grass----");
//    }
//}