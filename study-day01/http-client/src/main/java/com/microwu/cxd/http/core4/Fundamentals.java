package com.microwu.cxd.http.core4;

import org.apache.http.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.Map;

/**
 * Description: 基本原理
 *  1.1.1 结构体
 *  HTTP 消息由标头和可选主体组成。HTTP请求的消息标头由请求行和标头字段的集合组成。
 *  HTTP响应的消息标头由状态行和标头字段的集合组成
 *  所有HTTP 消息都必须包含协议版本，一些HTTP消息可以选择包含内容主体
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/25   10:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Fundamentals {

    /**
     * HTTP 请求消息
     * HTTP 请求是从客户端发送到服务器端的消息。该消息的第一行包括应用于资源的方法，
     * 资源的标识符，协议版本
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  10:26
     *
     * @param
     * @return  void
     */
    private static void test() {
        BasicHttpRequest request = new BasicHttpRequest("GET", "/", HttpVersion.HTTP_1_1);

        System.out.println(request.getRequestLine().getMethod());
        System.out.println(request.getRequestLine().getUri());
        System.out.println(request.getProtocolVersion());
        System.out.println(request.getRequestLine().toString());

    }

    /**
     * HTTP 响应消息
     * HTTP 响应是服务器在收到并解释了请求消息后发送回客户端的消息
     * 该消息的第一行有协议版本，数字状态码，相关的文本短语组成
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  10:30
     *
     * @param
     * @return  void
     */
    private static void test02() {
        BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());

    }

    /**
     * HTTP 消息的常用属性和方法
     * HTTP 消息可以包含很多标头，这些标头描述消息的属性
     * 例如内容长度，内容类型等
     *
     * HttpCore 提供了检索，添加，删除和枚举此类标头的方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  10:34
     *
     * @param
     * @return  void
     */
    private static void test03() {
        BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie", "c1=a;path=/;domain=localhost");
        response.addHeader("Set-Cookie", "c2=b;path=\"/\";domain=\"localhost\"");
        Header firstHeader = response.getFirstHeader("Set-Cookie");
        System.out.println(firstHeader);
        Header lastHeader = response.getLastHeader("Set-Cookie");
        System.out.println(lastHeader);

        // 获取所有标头的 方法
        System.out.println("======================= 遍历标头 ===========================");
        HeaderIterator iterator = response.headerIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        // 解析标头元素
        // HTTP 标头尽在需要时才标记为单独的标头元素。
        // 通过HTTP 连接接收的HTTP 标头在内部存储为字符数组，只有在访问器属性时才被延迟解析
        System.out.println("=========================== 解析标头 =================================");
        BasicHeaderElementIterator elementIterator = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));
        while (elementIterator.hasNext()) {
            HeaderElement headerElement = elementIterator.nextElement();
            System.out.println(headerElement.getName() + " " + headerElement.getValue());
            NameValuePair[] parameters = headerElement.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                System.out.println(" " + parameters[i]);
            }

        }

    }

    /**
     * HTTP 实体
     * HTTP 消息可以包含与请求或响应关联的内容实体。
     * 实体可以在某些请求和响应中找到，因为它们是可选的。
     * 使用实体的请求称为封闭实体的请求
     * HTTP 规范定义了两种实体封装方法：PUT 和 POST，通常期望响应包含内容实体
     * 此规则有例外，对HEAD方法的响应和204 No Content，304 Not Modified， 205 Rest Content 响应
     *
     * HttpCore 根据内容的来源将其区分为三种：
     *  > 流式：从流中接收内容，或及时生成内容。特别的，该类别包括从连接接受的实体。流式实体通常不可重复
     *  > 自包含的：内容在内从中或通过独立于连接或者其他实体的方式获取。自包含的实体通常是可重复的
     *  > 包装：内容是从另一个实体获取的
     *
     *  可重复实体：实体是可重复的，这意味这其内容是可以被读取多次的。这仅适用于自包含的实体（ByteArrayEntity或 StringEntity）
     *
     *  使用HTTP实体
     *  由于实体既可以表示二进制内容又可以表示字符内容，因此它支持字符编码
     *  当执行带有封闭内容的请求时，或者当请求成功时，将创建实体，并使用响应主体将结果发送回客户端
     *  要从实体中读取内容，可以通过HttpEntity#getContent 方法检索输入流，该方法返回InputStream，或者可以向该HttpEntity#writeTo(OutputStream)
     *  方法提供输出流，该方法将在所有内容均已写入给定流后返回。
     *  EntityUtils 公开了一些静态方法以简化提取从实体的内容或信息。通过InputStream 直接使用此类的方法，可以读取字符串或字节数组中的完整内容主体，
     *  而不用直接读取它们。当实体已经与传入消息接受，该方法HttpEntity#getContentType和GetContentLength 可用于读取所述公共元数据
     *  由于Content-Type 可以包含用语问们类型mime类型的字符编码text/html(text/plain)，因此该getContentEncoding 方法用于读取此信息
     *  如果标头不可用，返回-1
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  10:47
     *
     * @param
     * @return  void
     */
    private static void test04() throws IOException {
        StringEntity entity = new StringEntity("important message", Consts.UTF_8);

        System.out.println(entity.getContentType());
        System.out.println(entity.getContentLength());
        System.out.println(EntityUtils.toString(entity));
        System.out.println(EntityUtils.toByteArray(entity).length);
    }

    /**
     * 确保释放系统资源
     * 为了确保适当释放系统资源，必须关闭与实体关联的内容流
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  11:09
     *
     * @param
     * @return  void
     */
    private static void test05() throws IOException {
        HttpResponse response = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream inputStream = entity.getContent();
            try {
                // 做一些有用的东西
            } finally {
                inputStream.close();
            }
        }
    }

    /**
     * 创建实体
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  11:12
     *
     * @param
     * @return  void
     */
    private static void test06() throws FileNotFoundException, UnsupportedEncodingException {
        // BasicHttpEntity：通常，将此类用于从HTTP消息接收的实体
        // 该实体具有空的构造函数，构建后，它表示没有内容
        // 需要设置内容流，并选择长度
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        basicHttpEntity.setContent(new FileInputStream(""));
        basicHttpEntity.setContentLength(340);

        // ByteArrayEntity 是一个自包含的可重复实体，可以从给定的字节数组中获取其内容。将字节数组提供给构造函数
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(new byte[]{1, 2, 3}, ContentType.APPLICATION_OCTET_STREAM);

        // String 是一个自包含的，可重复的实体，从String 对象获取其内容
        StringBuilder sb = new StringBuilder();
        Map<String, String> env = System.getenv();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\r\n");
        }

        // 不带字符编码的构造，ISO-8859-1
        StringEntity stringEntity = new StringEntity(sb.toString());
        // 使用编码构造 text/plain
        StringEntity stringEntity2 = new StringEntity(sb.toString(), Consts.UTF_8);
        // 使用编码和MIME类型构造
        StringEntity stringEntity3 = new StringEntity(sb.toString(), "text/plain", Consts.UTF_8.toString());

        // InputStreamEntity 流化，不可重复的实体，从输入流中获取其内容
        // FileEntity 自包含的可重复实体，从文件中获取内容。需要提供文件的内容类型，xml = application/xml
        // HttpEntityWrapper 包装实体的基类
    }

    /**
     * Http协议处理器
     * HTTP协议拦截器是一种实现HTTP线衣特定方面的例程。
     * 通常，协议拦截器应作用于传入消息的一个特定表头或一组相关标头，或使用一个特定标头或一组相关标头填充输出消息
     * 协议拦截器还可以操作包含在消息中的内容实体。透明的内容压缩/解压缩就是一个很好的例子。通常，这是通过使用
     * 装饰器模式完成的，在该模式中，使用包装实体类来装饰原始实体
     *
     * HTTP协议处理器是实现责任链模式的协议拦截器的集合，其中每个单独的协议拦截器都应在其负责的HTTP协议的特定方面工作
     * 通常，拦截器的执行顺序无关紧要，只要他们不依赖于执行上下文状态即可
     * 协议拦截器必须是线程安全的。与Servlet 相似，协议拦截器不应使用实例变量
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/25  11:26
     *
     * @param
     * @return  void
     */
    public static void test07() throws IOException, HttpException {
        HttpProcessor processor = HttpProcessorBuilder.create()
                // 必须的协议拦截器
                .add(new RequestContent())
                .add(new RequestTargetHost())
                // 推荐的协议拦截器
                .add(new RequestConnControl())
                .add(new RequestUserAgent("MyAgent-Http/1/1"))
                // 可选的
                .add(new RequestExpectContinue(true))
                .build();

        HttpCoreContext context = HttpCoreContext.create();
        BasicHttpRequest request = new BasicHttpRequest("GET", "/");
        processor.process(request, context);

    }

    public static void main(String[] args) throws IOException {
//        test();
//        test02();
//        test03();
        test04();
    }

}