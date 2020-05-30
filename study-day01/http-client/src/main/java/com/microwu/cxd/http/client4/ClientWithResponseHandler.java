package com.microwu.cxd.http.client4;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Description: 演示如何使用响应处理程序处理HTTP 响应
 * 这是执行HTTP 请求和HTTP 响应推荐的方法。
 * 这种方法使调用者可以专注于摘要HTTP 响应的过程，并将系统资源释放任务委派给HttpClient
 * HTTP 响应处理程序的使用保证了所有情况下基础HTTP 连接都会自动释放回连接管理器
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/25   15:29
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClientWithResponseHandler {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet("http://httpbin.org/");

            System.out.println("Executing request " + httpGet.getRequestLine());

            // 创建一个自定义的响应处理者
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = httpResponse.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            String responseBody = httpClient.execute(httpGet, responseHandler);
            System.out.println("=================================================");
            System.out.println(responseBody);
        } finally {
            httpClient.close();
        }
    }

}