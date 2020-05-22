package com.microwu.cxd.http;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/22   10:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QuickStartDemo {

    public static void main(String[] args) throws Exception {
        // https://www.jianshu.com/p/6adb6dbc4140
        // 使用try-with-resource 关闭资源
        // 要求变量 实现AutoCloseable 接口，这样可以自动调用它们的close 方法
        // try-with-resources 将会自动u干笔 try() 中的资源，并且将先关闭后声明的资源
        // 反编译一下代码，发现其实还是 try-catch，调用重写的close 方法，
        // 只是不用手写了，编译器帮你补充
        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://httpbin.org/get");
            // 基础HTTP连接仍由相应对象保持，允许直接从网络套接字流式传输响应内容
            // 为了确保正确释放系统资源，用户必须从finally 调用close 方法
            // 注意：如果相应内容没有完全消耗，则底层连接不能安全的重新使用
            // 将被连接管理器关闭和丢弃
            try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final HttpEntity entity = response.getEntity();
                // 对响应体作一些消耗，确认它被完全消费了
                EntityUtils.consume(entity);
            }

            final HttpPost httpPost = new HttpPost("http://httpbin.org/post");
            final List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("username", "cxd"));
            nvps.add(new BasicNameValuePair("password", "123456"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try (final CloseableHttpResponse response = httpClient.execute(httpPost)) {
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                HttpEntity entity = response.getEntity();
                EntityUtils.consume(entity);
            }
        }
    }

}