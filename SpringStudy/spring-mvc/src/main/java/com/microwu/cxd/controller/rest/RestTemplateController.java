package com.microwu.cxd.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/23   10:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/rest")
public class RestTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateController.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 1. 从URI模板构建 URI
     * <p>
     * 涉及的知识点：
     * UriComponentsBuilder#encode() -> 首先对URI模板进行与编码，然后扩展时对URL变量进行严格编码
     * UriComponents#encode() -> 对扩展URI变量进行编码
     * 两个选项都会使用转义的八位字节替换非法字符，但是第一个选项还会替换出现在URI中保留的字符
     * <p>
     * 我认为平时要求的请求为utf-8/gbk，是对请求路径的编码，而不是对请求内容的编码
     *
     * @param
     * @return java.lang.String
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/23  10:23
     */
    @GetMapping("/uri")
    public String uri() {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString("https://example.com/hotels/{hotel}")
                .queryParam("q", "{q}")
                .encode()
                .build();

        URI uri = uriComponents.expand("Westin", "123").toUri();

//        URI uri = UriComponentsBuilder.fromUriString("http://10.0.0.113/{test}/test222.php?a={aaa}").build("test", "aaa");
        logger.info("{}", uri);
        return "success";
    }

    /**
     * 1. URI
     * RestTemplate 默认构造函数 是基于HttpURLConnection 执行请求，
     * 可以使用其他HTTP库 - RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
     * <p>
     * 注意：URI模板是自动编码的，你也可以使用uriTemplateHandler 自定义 编码方式，也可以准备一个 URI参数
     * <p>
     * 注意两个字段
     * Accept：请求头，代表客户端希望接受的数据类型
     * Content-Type：响应头，代表服务端发送的数据类型
     *
     * 我的理解：作为发送端，必须要指定Content-Type，否则接收端不知道如何处理数据
     *
     * @param
     * @return java.lang.String
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/23  11:42
     */
    @GetMapping("/request")
    public String request() {
        String result = restTemplate.getForObject("http://10.0.0.113/test/test222.php?aaa=b", String.class);
        logger.info("{}", result);
        return "success";
    }

    /**
     * 标头
     * 使用exchange 指定请求头
     *
     * xxxForObject: 直接返回乡音体
     * xxxForEntity：返回相应行，响应头，响应码，响应体等等
     *
     * @param
     * @return java.lang.String
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/23  13:24
     */
    @GetMapping("/header")
    public String header() {
        String uriTemplate = "http://10.0.0.113/test/test222.php?aaa={}";
        URI uri = UriComponentsBuilder.fromUriString(uriTemplate).build().expand("aaa").toUri();

        RequestEntity<Void> requestEntity = RequestEntity.post(uri).header("MyReader", "MyValue").build();

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        String responseHeader = response.getHeaders().getFirst("Content-Type");
        String body = response.getBody();

        logger.info("Content-Type: {}, body: {}", responseHeader, body);
        return "success";
    }

    /**
     * body
     * 无需显示设置请求的Content-Type header。在大多数情况下，可以根据 源Object 类型找到兼容的消息转换器
     * 并且所选的消息转换器会相应地设置内容类型。如果有必要，可以使用exchange 显示提供 Content-Type 请求标头，
     * 从而影响选择那个消息转换器 - 我的理解，不需要显示的设置 Content-Type属性，它会根据源对象找到兼容
     * 的消息转换器，并设置对应的内容类型
     * <p>
     * 同时也不需要显示设置 Accept 请求头，可以根据响应类型找到兼容的消息转换器
     * HttpMessageConverter 用于阅读和写作，在RestTemplate 和 SpringMVC 中使用。
     * - StringHttpMessageConverter：读取和写入String类型。默认支持text/* 类型，并用 Content-Type:text/plain 写入
     * - FormHttpMessageConverter: 读取和写入表单数据。默认支持 application/x-www-form-urlencoded 类型，可以从
     * MultiValueMap<String, String> 读取和写入，但是不支持从MultiValueMap<Sting, Object>读，支持写
     * - json 类型，官网上没说
     * <p>
     * 多部分
     * 要发送多部分数据，需要提供 MultiValueMap<String, Object>，可以是 Object，Resource或者headers
     * 大多数情况下，不必指定Content-Type，内容类型可以自动确定，如果有必要，可以明确指定
     * MultiValueMap 包含一个非String值时，默认multipart/fom-data。如果有必要，也可以明确设置
     * 经过抓包发现，其实MultiValueMap 其实每一个属性都有对应的 Content-Type，而总的Content-Type 为 multipart/form-data
     *
     * 存在的问题：没有使用json 进行数据交互，这部分参考圣杰的代码
     * 我认为 对于restTemplate 指定Content-Type 就是用来指定消息转换器
     *
     * @param
     * @return java.lang.String
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/23  13:48
     */
    @GetMapping("/body")
    public String body() {
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("fieldPart", "fieldValue");
        parts.add("filePart", new FileSystemResource(new File("G:\\exe\\txt\\id_rsa")));

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_XML);
//        parts.add("xmlPart", new HttpEntity<>());
        String response = restTemplate.postForObject("http://10.0.0.113/test/test222.php", parts, String.class);
        logger.info("{}", response);
        return "success";
    }
}