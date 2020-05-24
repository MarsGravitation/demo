package com.microwu.cxd.http;

import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpResponseParser;
import org.apache.hc.core5.http.io.HttpMessageParser;
import org.apache.hc.core5.http.io.HttpMessageParserFactory;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicLineParser;
import org.apache.hc.core5.http.message.RequestLine;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.CharArrayBuffer;
import org.apache.hc.core5.util.TimeValue;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/22   11:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClientConfiguration {

    public static void main(String[] args) throws Exception {
        // 使用自定义消息解析器/编写器自定义HTTP的方式被解析并写入数据流
        // 下面使用 函数表达式进行配置

        // 创建 HTTP/1.1 协议配置
        final Http1Config http1Config = Http1Config.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        // 创建字符集配置
        final CharCodingConfig charCodingConfig = CharCodingConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(StandardCharsets.UTF_8)
                .build();

        // 使用自定义连接工厂
        // 配置参数，解析器和编写器
        ManagedHttpClientConnectionFactory connFactory =
                new ManagedHttpClientConnectionFactory(http1Config, charCodingConfig, new DefaultHttpRequestWriterFactory(), http1Config1 -> {
                    BasicLineParser basicLineParser = new BasicLineParser() {
                        @Override
                        public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
                            try {
                                return super.parseHeader(buffer);
                            } catch (final ParseException e) {
                                return new BasicHeader(buffer.toString(), null);
                            }
                        }
                    };
                    return new DefaultHttpResponseParser(basicLineParser, DefaultClassicHttpResponseFactory.INSTANCE, http1Config);
                });

        //
        final SSLContext sslContext = SSLContexts.createSystemDefault();

        // 创建一个支持自定义socket 连接工厂
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();

        // 使用自定义DNS 解析器
        final DnsResolver resolver = new SystemDefaultDnsResolver();

        // 创建一个自定义 连接管理者
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, PoolConcurrencyPolicy.STRICT, PoolReusePolicy.LIFO, TimeValue.ofMinutes(5),
                null, resolver, connFactory
        );

        // 创建一个socket 配置
        final SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        // 配置连接管理者使用socket 配置
        connectionManager.setDefaultSocketConfig(socketConfig);
        // 闲置1秒后验证连接
        connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(1));

        // 配置永久连接的最大总数或每个路由限制
        // 可以保留在池中或者由连接器管理租用
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(10);
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);

        // 如果有必要，自定义cookie 存储
        final CookieStore cookieStore = new BasicCookieStore();
        // 自定义凭据提供程序
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 创建全局请求配置
        final RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(StandardCookieSpec.STRICT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.BASIC))
                .build();

        // 使用给定的自定义依赖项和配置创建 HttpClient
        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
//                .setProxy(new HttpHost("myproxy", 8080))
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            final HttpGet httpGet = new HttpGet("http://httpbin.org/get");
            // 可以配置请求级别覆盖请求配置，它们由于客户端级别的一组
            // requestConfig
            HttpClientContext context = HttpClientContext.create();
            // 设置本地上下文级别的上下文属性
            // 优先于客户端级别设置的优先级
            // cookie, credentialsProvider

            System.out.println("执行请求 " + httpGet.getMethod() + " " + httpGet.getUri());
            try (final CloseableHttpResponse response = httpclient.execute(httpGet, context)) {
                System.out.println("=========================================================");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                System.out.println(EntityUtils.toString(response.getEntity()));

                // 一旦执行了请求，本地上下文可以通过请求执行检查更新状态和受影响的各种对象
                context.getRequest();
                context.getHttpRoute();
                context.getAuthExchanges();
                context.getCookieOrigin();
                context.getUserToken();
            }

        }


    }

}