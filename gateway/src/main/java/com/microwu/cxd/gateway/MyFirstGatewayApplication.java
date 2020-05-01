package com.microwu.cxd.gateway;

import com.microwu.cxd.gateway.filter.RequestTimeGatewayFilterFactory;
import com.microwu.cxd.gateway.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Gateway 是 Spring Cloud 推出的第二代网关框架，取代 Zuul 网关
 * 网关具有 路由转发，权限校验，限流控制等作用
 */
@SpringBootApplication
@RestController
public class MyFirstGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyFirstGatewayApplication.class, args);
    }

    /**
     * 使用RouteLocatorBuilder 去创建路由
     * <p>
     * 请求 /get 请求会转发到 http://httpbin.org/get，route 配置上，我们增加了一个filter
     * 该 filter 会将请求添加到header，hello = world
     *
     * 作用：
     *  1. 协议转换，路由转发
     *  2. 流量聚合，对流量进行监控，日志输出
     *  3. 作为整个系统的前端工程，对流量进行控制，有限流作用
     *  4. 作为系统的前段边界，外部流量只能通过网关才能访问系统
     *  5. 可以在网关层做权限判断
     *  6. 可以在网关层做缓存
     *
     *  https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.2.RELEASE/reference/html/
     *
     *  客户端向 Spring Cloud Gateway 发出请求。如果Gateway Handler Mapping 确定请求与路由匹配（这个时候就用到predicate），
     *  则将其发送到Gateway web  handler处理。Gateway web handler 处理请求时会经过一系列过滤器链。过滤器链被虚线划分的原因
     *  是过滤器链可以在发送代理请求之前或者之后执行过滤逻辑。限制性 pre 过滤器逻辑，然后进行代理请求，然后执行post
     *
     *  https://forezp.obs.myhuaweicloud.com/img/jianshu/12191355-7c74ff861a209cd9.png
     *
     *  只有当满足 predicate，才会交给 router 处理。每一种predicate 都会对当前客户端请求进行判断，满足当前要求，交给当前处理器处理
     *  如果有多个predicate
     *
     *
     * @param builder
     * @return org.springframework.cloud.gateway.route.RouteLocator
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/30  9:45
     */
//    @Bean
//    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(p -> p
//                        .path("/get")
//                        .filters(f -> f.addRequestHeader("hello", "world"))
//                        .uri("http://httpbin.org/get"))
//                .route(p -> p
//                        .host("*.hystrix.com")
//                        .filters(f -> f
//                                .hystrix(config -> config
//                                        .setName("cmd")
//                                        .setFallbackUri("forward:/fallback"))).uri("http://a.b"))
//                .build();
//    }

    /**
     * hystrix：
     *  每个命令在独立线程中进行
     *  当服务错误率超过了一定阈值，自动或手动跳闸，停止请求该服务
     *  hystrix 为每个依赖维护了小型线程池
     *  监控
     *  回退机制
     *  自我修复机制
     *
     *  第二个案例是请求头host -> *.hystrix.com，走第二个过滤器，
     *  但是uri 是一个无效的，超过阈值后自动跳闸，走回退机制
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/30  10:06
     *
     * @param
     * @return  reactor.core.publisher.Mono<java.lang.String>
     */
//    @RequestMapping("/fallback")
//    public Mono<String> fallback() {
//        return Mono.just("fallback");
//    }

//    @Bean
//    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(r -> r.path("/customer/**")
//                        .filters(f -> f.filter(new RequestTimeFilter())
//                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
//                        .uri("http://httpbin.org:80/get")
//                        .order(0)
//                        .id("customer_filter_router")
//                )
//                .build();
//    }

    @Bean
    public RequestTimeGatewayFilterFactory elapsedGatewayFilterFactory() {
        return new RequestTimeGatewayFilterFactory();
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
