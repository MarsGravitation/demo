package com.microwu.cxd.http.client4;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/25   15:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClientConfiguration {

    public static void main(String[] args) throws IOException {
        // 使用自定义消息解析和写入器
        DefaultHttpResponseParserFactory responseParserFactory = new DefaultHttpResponseParserFactory() {
            @Override
            public HttpMessageParser<HttpResponse> create(SessionInputBuffer buffer, MessageConstraints constraints) {
                BasicLineParser basicLineParser = new BasicLineParser() {
                    @Override
                    public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }
                };
                return new DefaultHttpResponseParser(
                        buffer, basicLineParser, DefaultHttpResponseFactory.INSTANCE, constraints);
            }
        };

        DefaultHttpRequestWriterFactory requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // 使用自定义连接工厂
        // 初始化传出HTTP 连接
        ManagedHttpClientConnectionFactory connectionFactory = new ManagedHttpClientConnectionFactory(requestWriterFactory, responseParserFactory);

        // 客户端HTTP 连接对象在完全初始化时可以绑定到一个任意的网络套接字
        // 网络套接字初始化的过程控制其与远程地址的连接以及与本地地址的绑定
        // 由连接套接字工厂提供

        // 安全连接的SSL 上下文可以基于系统或者应用程序特定的属性
        SSLContext sslContext = SSLContexts.createSystemDefault();

        // 创建自定义连接套接字工厂的注册表以供支持协议方案
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();

        // 使用自定义DNS 解析器覆盖系统DNS 解析
        SystemDefaultDnsResolver dnsResolver = new SystemDefaultDnsResolver() {
            @Override
            public InetAddress[] resolve(String host) throws UnknownHostException {
                if ("myHost".equalsIgnoreCase(host)) {
                    return new InetAddress[] {InetAddress.getByAddress(new byte[] {127, 0, 0, 1})};
                } else {
                    return super.resolve(host);
                }
            }
        };

        // 创建一个自定义的连接管理者
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);

        // 创建socket 配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);

        // 配置永久连接的最大总数，每个路由限制
        // 可以保留在池中或者由连接管理器租用
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(10);
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);

        // 如果必要使用自定义cookie
        BasicCookieStore cookieStore = new BasicCookieStore();
        // 如果必要使用自定义凭据提供程序
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 创建全局请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        // 创建HttpClient 用给定的自定义依赖和配置
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
//                .setProxy(new HttpHost("myProxy", 8080))
                .setDefaultRequestConfig(requestConfig)
                .build();

        try {
            HttpGet httpGet = new HttpGet("http://httpbin.org/get");
            CloseableHttpResponse response = httpClient.execute(httpGet, HttpCoreContext.create());
            try {
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }

        } finally {
            httpClient.close();
        }

    }
}