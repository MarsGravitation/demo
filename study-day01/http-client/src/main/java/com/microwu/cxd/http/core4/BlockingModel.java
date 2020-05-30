package com.microwu.cxd.http.core4;

import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.*;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Description: 阻塞式IO 模型
 *  Java中阻塞式I/O 代表了高效且方便的I/O 模型，非常适合并行连接数相对适中的高性能应用程序。
 *  现代JVM 具有高效的上下文切换能力，并且阻塞式 I/O 模型应在原始数据吞吐量方面提供最佳性能，
 *  只要并发连接数量少于以前，并且连接主要在忙于传输数据。但是对于大多数情况下连接保持空闲的应用程序，
 *  上下文切换的开销可能会变大，非阻塞式I/O 可能是个更好的选择
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/25   11:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BlockingModel {

    /**
     * HTTP 连接负责HTTP 消息的序列化和反序列化。
     * 一般很少直接使用HTTP 连接对象。有用于执行和处理HTTP 请求的更高级别的协议组件
     * 但是在某些情况下，可能需要与HTTP 连接进行直接交互，例如，以访问诸如连接状态，套接字超时
     * 或者本地和远程地址之类的属性
     *
     * 请记住，HTTP 连接不是线程安全的，这一点很重要。我们强烈建议将与HTTP 连接对象的所有交互限制在
     * 一个线程中。HttpConnection 可以从另一个线程安全调用的接口及其子接口的唯一方法是HttpConnection#shutdown
     *
     * HttpCore 不提供对打开连接的完全支持，因为当涉及到一个或多个身份验证或隧道代理是，建立新连接的过程
     * （尤其是在客户端）可能非常的复杂。相反，阻塞式HTTP 连接可以绑定到任意网络套接字
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  13:45
     *
     * @param
     * @return  void
     */
    private static void test() throws IOException, HttpException {
        Socket socket = null;

        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
        conn.bind(socket);

        System.out.println(conn.isOpen());

        HttpConnectionMetrics metrics = conn.getMetrics();
        System.out.println(metrics.getRequestCount());
        System.out.println(metrics.getResponseCount());
        System.out.println(metrics.getReceivedBytesCount());
        System.out.println(metrics.getSentBytesCount());

        // 客户端和服务器的HTTP 连接分为两个阶段，发送和接受消息。
        // 首先发送消息头，根据消息头的属性，可能跟随消息主体
        // 请注意，使用关闭基础内容流以信号通知消息处理已完成
        // 直接从基础连接的输入流中流式传输其内容的HTTP 实体必须确保它们完全消耗消息正文的内容
        // 以使该链接潜在的可重用
        BasicHttpRequest request = new BasicHttpRequest("GET", "/");
        conn.sendRequestHeader(request);
        HttpResponse response = conn.receiveResponseHeader();
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            // 确保消耗内容
            // 基础连接可以重复使用
            EntityUtils.consume(entity);
        }
    }

    /**
     * 服务端处理过程
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  14:11
     *
     * @param
     * @return  void
     */
    private static void test02() throws IOException, HttpException {
        Socket socket = null;

        DefaultBHttpServerConnection connection = new DefaultBHttpServerConnection(8 * 1024);
        connection.bind(socket);
        HttpRequest request = connection.receiveRequestHeader();
        if (request instanceof HttpEntityEnclosingRequest) {
            connection.receiveRequestEntity((HttpEntityEnclosingRequest) request);
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        }

        BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 200, "OK");
        response.setEntity(new StringEntity("Got it"));
        connection.sendResponseHeader(response);
        connection.sendResponseEntity(response);
    }

    /**
     * HTTP 连接使用该 HttpEntity 接口管理内容传输的过程。
     * HTTP 连接生成一个实体对象，该对象封装了传入消息的内容流。
     * 请注意，HttpServerConnection#receiveRequestEntity, receiveResponseEntity 没有检索缓冲区中任何输入的数据
     * 它们仅基于传入消息的属性注入适当的内容编码器，可以通过使用从封闭实体的内容输入流中读取内容来检索内容HttpEntity#getContent
     * 数据的数据将自动且对数据使用者完全透明的进行解码。同样，HTTP 连接依赖HttpEntity#writeTo(OutputStream) 生成外发消息内容的方法
     * 如果外发邮件包含一个实体，则内容将根据邮件的属性进行自动编码
     *
     * HTTP 连接的默认实现支持HTTP/1.1 规范定义的三种内容传输机制：
     *  > Content-Length 定界的：内容实体的末尾有 Content-Length 标头的值确定
     *  > 身份编码：通过关闭基础流来区分试题内容的末尾
     *  > 块编码：内容按小块发送
     *  根据消息附带的实体属性，将自动创建适当的内容流类
     *
     * 可以通过调用优雅的终止HTTP连接，也可以通过调用 HttpConnection#close HttpConnection#shudown 强制终止
     * 前者尝试终止前刷新所有缓冲的数据，并且可能无限期的阻塞。close 不是线程安全的，在不刷新内部缓冲区的情况下
     * 终止连接，并在不造成长时间阻塞的情况下尽快将控制权返回给调用方
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  14:18
     *
     * @param
     * @return  void
     */
    private static void test03() {

    }

    /**
     * HttpService 是基于阻塞I/O 模型的服务端HTTP 协议处理程序
     * HttpService 依靠 HttpProcessor 实例为所有传出消息生成强制性协议标头
     * 并对所有传入和传出消息应用通用的跨领域消息转换，
     * 而HTTP 请求处理程序应负责处理特定于应用程序的内容
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  14:31
     *
     * @param   	
     * @return  void
     */
    private static void test04() throws IOException, HttpException {
        HttpProcessor processor = HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("MyServer-HTTP/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl())
                .build();

//        HttpService httpService = new HttpService(processor, null);

        // HTTP 请求处理程序，表示用于处理一组特定HTTP 请求的例程
        HttpRequestHandler requestHandler = new HttpRequestHandler() {
            public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                httpResponse.setStatusCode(HttpStatus.SC_OK);
                httpResponse.setEntity(new StringEntity("some important message", ContentType.TEXT_PLAIN));
            }
        };

        // 请求处理程序解析器
        UriHttpRequestHandlerMapper handlerMapper = new UriHttpRequestHandlerMapper();
        handlerMapper.register("/service/*", requestHandler);

        HttpService httpService = new HttpService(processor, handlerMapper);

        // 使用Http 服务处理请求
        // 完全初始化和配置后，HttpService 可以用于执行和处理活动HTTP 连接的请求
        HttpServerConnection connection = null;
        HttpContext context = null;

        boolean active = true;
        try {
            while (active && connection.isOpen()) {
                httpService.handleRequest(connection, context);
            }
        } finally {
            connection.shutdown();
        }
    }

    /**
     * HTTP 请求执行器
     *
     * HttpRequestExecutor 依赖于HttpProcessor 实例为所有传出生成强制性协议头消息
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  14:44
     *
     * @param
     * @return  void
     */
    private static void test05() throws IOException, HttpException {
        HttpClientConnection connection = null;

        HttpProcessor processor = HttpProcessorBuilder.create()
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("MyClient/1.1"))
                .add(new RequestExpectContinue(true))
                .build();

        HttpRequestExecutor executor = new HttpRequestExecutor();

        BasicHttpRequest request = new BasicHttpRequest("GET", "/");
        HttpCoreContext context = HttpCoreContext.create();
        executor.preProcess(request, processor, context);
        HttpResponse response = executor.execute(request, connection, context);
        executor.postProcess(response, processor, context);

        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
    }

    /**
     * ConnectionReuseStrategy 旨在确定完成当前消息的传输后，是否可以将基础链接重新用于处理其他消息
     * 默认的连接重用策略会尽可能的是连接保持活动状态。首先，它检查用于传输消息的HTTP 协议版本
     * 1.1 默认长连接，1.0 不是；其次它检查Connection 标头的值。对等方可以通过发送 Kepp-Alive 或者 Close
     * 决定是否重用；第三，根据封闭实体的属性来决定连接是否可以安全重用
     *
     * 高效的客户端HTTP 传输通常需要有效的重新使用持久性连接。HttpCore 通过提供对持久HTTP 连接池的管理支持
     * 促进了连接重用过程。连接池实现是线程安全的，可以由多个使用者同时使用
     *
     * 默认情况下，该池总共允许20 个并发连接，而每个唯一路由仅允许两个并发丽娜姐。这两个连接限制是HTTP 规范限制的
     * 可以在运行时更改池配置，以根据特定的应用程序上下文允许更多的并发连接
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  14:58
     *
     * @param
     * @return  void
     */
    private static void test06() throws ExecutionException, InterruptedException {
        HttpHost localhost = new HttpHost("localhost");
        BasicConnPool connPool = new BasicConnPool();
        connPool.setMaxTotal(200);
        connPool.setDefaultMaxPerRoute(10);
        connPool.setMaxPerRoute(localhost, 20);
        // 请注意，连接池不会主动退出过期的连接
        // 即使无法将过期的连接租借给请求者，池也可能随着时间推移积累陈旧的连接
        // 建议在长时间不活动后从池中前置退出过期和空闲的连接
        connPool.closeExpired();
        connPool.closeIdle(1, TimeUnit.SECONDS);
        Future<BasicPoolEntry> future = connPool.lease(localhost, null);
        BasicPoolEntry poolEntry = future.get();

        // 请注意，连接池无法知道是否仍在使用连接，连接池用户有责任确保不需要连接后将其释放回池，即使该链接不可重用
        try {
            HttpClientConnection connection = poolEntry.getConnection();
        } finally {
            connPool.release(poolEntry, true);
        }

    }

    /**
     * TLS/SSL 支持
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  15:15
     *
     * @param
     * @return  void
     */
    private static void test07() throws IOException {
        SSLContext sslContext = SSLContexts.createSystemDefault();
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) socketFactory.createSocket("somehost", 443);
        // 强制执TLS 并禁用 SSL
        socket.setEnabledProtocols(new String[]{});
        // 实施强密码
        socket.setEnabledCipherSuites(new String[]{});

        DefaultBHttpClientConnection connection = new DefaultBHttpClientConnection(8 * 1024);
        connection.bind(socket);
    }

    public static void main(String[] args) {

    }
}