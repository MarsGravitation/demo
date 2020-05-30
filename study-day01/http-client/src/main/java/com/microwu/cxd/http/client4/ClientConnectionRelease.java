package com.microwu.cxd.http.client4;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description: 手动释放连接
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/25   15:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClientConnectionRelease {

    private static final Logger logger = LogManager.getLogger(ClientConnectionRelease.class);

    public static void main(String[] args) throws Exception {
        logger.info("创建HttpClient。。。。。。");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet("http://httpbin.org/get");

            System.out.println("Executing request " + httpGet.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                System.out.println("===============================================");
                System.out.println(response.getStatusLine());

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    try {
                        int read = inputStream.read();
                    } catch (IOException e) {
                        throw e;
                    } finally {
                        inputStream.close();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }

    }
}