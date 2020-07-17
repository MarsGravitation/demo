package com.microwu.cxd.chinese.httpclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/6   11:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JokeVerticle extends AbstractVerticle {

    private HttpRequest<JsonObject> request;

    @Override
    public void start() throws Exception {
        // 获取 WebClient
        request = WebClient.create(vertx)
                // 在端口 443 上对路径 / 主机 icanhazdadjoke.com 的 HTTP GET 请求
                .get(443, "icanhazdadjoke.com", "/")
                // SSL 加密
                .ssl(true)
                // 表明我们需要 JSON 数据
                .putHeader("Accept", "application/json")
                // 响应自动转换为 JSON
                .as(BodyCodec.jsonObject())
                // 期望 200 状态码
                .expect(ResponsePredicate.SC_OK);

        // 将结果写入控制台
        vertx.setPeriodic(3000, id -> fetchJoke());
    }

    private void fetchJoke() {
        request.send(httpResponseAsyncResult -> {
            if (httpResponseAsyncResult.succeeded()) {
                System.out.println(httpResponseAsyncResult.result().body().getString("joke"));
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new JokeVerticle());
    }
}