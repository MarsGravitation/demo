package com.microwu.cxd.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/30   11:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RequestTimeFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put("requestTimeBegin", Instant.now().toEpochMilli());
        return chain.filter(exchange).then(
                // 相当于 post 过滤器
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute("requestTimeBegin");
                    if (startTime != null) {
                        System.out.println(exchange.getRequest().getURI().getRawPath() + ":" + (Instant.now().toEpochMilli() - startTime) + "ms");
                    }
                })
        );
    }

    @Override
    public int getOrder() {
        return 0;
    }
}