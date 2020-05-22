package com.microwu.cxd.http;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description: 演示手动处理HTTP响应的情况下如何确保基础HTTP连接释放回连接管理器
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/22   11:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClientConnectionRelease {

    public static void main(String[] args) throws Exception {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet("http://httpbin.org/get");
            System.out.println("Executing request " + httpGet.getMethod() + " " + httpGet.getUri());
            try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println("-----------------------------------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());

                final HttpEntity entity = response.getEntity();

                // 如果响应不包含一个实体，不需要关心连接释放
                if (entity != null) {
                    try (final InputStream inputStream = entity.getContent()) {
                        int read = inputStream.read();
                        System.out.println(read);
                    } catch (final IOException e) {
                        // 如果发生IOException，连接将被释放，自动返回连接管理器
                        throw e;
                    }
                }
            }
        }
    }
}