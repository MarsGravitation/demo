package com.microwu.cxd.http.client4;

import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description: HttpClient 高并发下性能优化 - HTTP 连接池
 *  情景：
 *      1. 过多的长连接会占用服务器资源，导致其他服务受阻
 *      2. 适用于请求是经常访问同一主机的情况
 *      3. 并发数不高的情况下资源利用率低下
 *
 *  优点：
 *      1. 复用 http 连接，省去了 tcp 的 3 次握手和 4 次挥手的时间，极大降低了请求响应的时间
 *      2. 自动管理 tcp 连接，不用人为的释放/创建连接
 *
 *  注意点：
 *      1. HttpClient 实例必须是单例，且该实例必须绑定连接池
 *      2. close 可以直接把连接放回连接池
 *      3. 由于服务器一般不会允许无期限的长连接，所以需要开启监控线程，每隔一段时间就检测一下连接池
 *      中的连接情况，及时关闭异常连接和长时间空闲的连接
 *
 * https://www.jianshu.com/p/c852cbcf3d68
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   11:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HttpConnectionPoolUtil {

    /**
     * 发送请求的客户端单例
     */
    private static CloseableHttpClient httpClient;

    /**
     * 连接池管理类
     */
    private static PoolingHttpClientConnectionManager manager;

    private static ScheduledExecutorService monitorExecutor;

    /**
     * 相当于线程锁，用于线程安全
     */
    private final static Object syncLock = new Object();

    /**
     * 对 HTTP 请求进行基本设置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  11:15
     *
     * @param   	httpRequestBase
     * @return  void
     */
    private static void setRequestConfig(HttpRequestBase httpRequestBase) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1)
                .setConnectTimeout(1)
                .setSocketTimeout(1).build();

        httpRequestBase.setConfig(requestConfig);
    }

    public static CloseableHttpClient getHttpClient(String url) {
        if (httpClient == null) {
            // 多线程下多个线程同时调用 getHttpClient 容易导致重复创建，加同步锁
            synchronized (syncLock) {
                httpClient = createHttpClient(url);
                // 开启监控线程，对异常和空闲线程进行关闭
                monitorExecutor = Executors.newScheduledThreadPool(1);
                monitorExecutor.scheduleAtFixedRate(() -> {
                    // 关闭异常连接
                    manager.closeExpiredConnections();
                    // 关闭 5s 空闲的连接
                    manager.closeIdleConnections(5, TimeUnit.SECONDS);
                }, 1, 1, TimeUnit.SECONDS);
            }
        }
        return httpClient;
    }

    /**
     * 构建 HttpClient 实例
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  11:45
     *
     * @param   	url
     * @return  org.apache.http.impl.client.CloseableHttpClient
     */
    public static CloseableHttpClient createHttpClient(String url) {
        String hostName = url.split("/")[2];
        int port = 80;
        if (hostName.contains(":")) {
            String[] args = hostName.split(":");
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        }
        PlainConnectionSocketFactory plainConnectionSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainConnectionSocketFactory)
                .register("https", sslConnectionSocketFactory).build();

        manager = new PoolingHttpClientConnectionManager(registry);
        // 设置连接参数
        // 最大连接数
        manager.setMaxTotal(100);
        // 路由最大连接数
        manager.setDefaultMaxPerRoute(20);

        HttpHost httpHost = new HttpHost(hostName, port);
        manager.setMaxPerRoute(new HttpRoute(httpHost), 5);

        // 请求失败时，进行请求重试

        // 自定义 keep alive 策略
        return HttpClients.custom().setConnectionManager(manager).setRetryHandler(null).setKeepAliveStrategy((response, httpContext) -> {
            BasicHeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement headerElement = it.nextElement();
                String name = headerElement.getName();
                String value = headerElement.getValue();
                if (value != null && name.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000;
                }
            }
            return 60 * 1000;
        }).build();
    }

    /**
     * 设置 post 请求参数
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  11:45
     *
     * @param   	httpPost
     * @param 		params
     * @return  void
     */
    public static void setPostParams(HttpPost httpPost, Map<String, String> params) throws UnsupportedEncodingException {
        final List<NameValuePair> nvps = new ArrayList<>();
        params.keySet().stream().forEach(key -> {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        });

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

    }

//    public static JsonObject

    /**
     * 关闭连接池
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/30  13:41
     *
     * @param
     * @return  void
     */
    public static void closeConnectionPool() {
        try {
            httpClient.close();
            manager.close();
            monitorExecutor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}