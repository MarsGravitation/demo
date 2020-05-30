package com.microwu.cxd.http.client5;

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

//    public static void main(String[] args) throws Exception {
//        // 使用自定义消息解析器/编写器自定义HTTP的方式被解析并写入数据流
//        // 下面使用 函数表达式进行配置
//
//        // 创建 HTTP/1.1 协议配置
//        final Http1Config http1Config = Http1Config.custom()
//                .setMaxHeaderCount(200)
//                .setMaxLineLength(2000)
//                .build();
//        // 创建字符集配置
//        final CharCodingConfig charCodingConfig = CharCodingConfig.custom()
//                .setMalformedInputAction(CodingErrorAction.IGNORE)
//                .setUnmappableInputAction(CodingErrorAction.IGNORE)
//                .setCharset(StandardCharsets.UTF_8)
//                .build();
//
//        // 使用自定义连接工厂
//        // 配置参数，解析器和编写器
//        ManagedHttpClientConnectionFactory connFactory =
//                new ManagedHttpClientConnectionFactory(http1Config, charCodingConfig, new DefaultHttpRequestWriterFactory(), http1Config1 -> {
//                    BasicLineParser basicLineParser = new BasicLineParser() {
//                        @Override
//                        public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
//                            try {
//                                return super.parseHeader(buffer);
//                            } catch (final ParseException e) {
//                                return new BasicHeader(buffer.toString(), null);
//                            }
//                        }
//                    };
//                    return new DefaultHttpResponseParser(basicLineParser, DefaultClassicHttpResponseFactory.INSTANCE, http1Config);
//                });
//
//        //
//        final SSLContext sslContext = SSLContexts.createSystemDefault();
//
//        // 创建一个支持自定义socket 连接工厂
//        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.INSTANCE)
//                .register("https", new SSLConnectionSocketFactory(sslContext))
//                .build();
//
//        // 使用自定义DNS 解析器
//        final DnsResolver resolver = new SystemDefaultDnsResolver();
//
//        // 创建一个自定义 连接管理者
//        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
//                socketFactoryRegistry, PoolConcurrencyPolicy.STRICT, PoolReusePolicy.LIFO, TimeValue.ofMinutes(5),
//                null, resolver, connFactory
//        );
//
//        // 创建一个socket 配置
//        final SocketConfig socketConfig = SocketConfig.custom()
//                .setTcpNoDelay(true)
//                .build();
//        // 配置连接管理者使用socket 配置
//        connectionManager.setDefaultSocketConfig(socketConfig);
//        // 闲置1秒后验证连接
//        connectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(1));
//
//        // 配置永久连接的最大总数或每个路由限制
//        // 可以保留在池中或者由连接器管理租用
//        connectionManager.setMaxTotal(100);
//        connectionManager.setDefaultMaxPerRoute(10);
//        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);
//
//        // 如果有必要，自定义cookie 存储
//        final CookieStore cookieStore = new BasicCookieStore();
//        // 自定义凭据提供程序
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        // 创建全局请求配置
//        final RequestConfig requestConfig = RequestConfig.custom()
//                .setCookieSpec(StandardCookieSpec.STRICT)
//                .setExpectContinueEnabled(true)
//                .setTargetPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.DIGEST))
//                .setProxyPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.BASIC))
//                .build();
//
//        // 使用给定的自定义依赖项和配置创建 HttpClient
//        try (final CloseableHttpClient httpclient = HttpClients.custom()
//                .setConnectionManager(connectionManager)
//                .setDefaultCookieStore(cookieStore)
//                .setDefaultCredentialsProvider(credentialsProvider)
////                .setProxy(new HttpHost("myproxy", 8080))
//                .setDefaultRequestConfig(requestConfig)
//                .build()) {
//            final HttpGet httpGet = new HttpGet("http://httpbin.org/get");
//            // 可以配置请求级别覆盖请求配置，它们由于客户端级别的一组
//            // requestConfig
//            HttpClientContext context = HttpClientContext.create();
//            // 设置本地上下文级别的上下文属性
//            // 优先于客户端级别设置的优先级
//            // cookie, credentialsProvider
//
//            System.out.println("执行请求 " + httpGet.getMethod() + " " + httpGet.getUri());
//            try (final CloseableHttpResponse response = httpclient.execute(httpGet, context)) {
//                System.out.println("=========================================================");
//                System.out.println(response.getCode() + " " + response.getReasonPhrase());
//                System.out.println(EntityUtils.toString(response.getEntity()));
//
//                // 一旦执行了请求，本地上下文可以通过请求执行检查更新状态和受影响的各种对象
//                context.getRequest();
//                context.getHttpRoute();
//                context.getAuthExchanges();
//                context.getCookieOrigin();
//                context.getUserToken();
//            }
//
//        }
//
//
//    }

}