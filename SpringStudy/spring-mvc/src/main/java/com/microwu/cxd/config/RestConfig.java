package com.microwu.cxd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/23   11:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class RestConfig {

    /**
     * 可以通过 UriBuilderFactory 创建 UriBuilder，用户配置共享配置，例如
     * 基本URL，编码首选项和其他详细信息
     *
     * 感觉这个实际用的并不是很多，因为实际情况基本URL多数不一样，编码也不确定
     *
     * restTemplate 使用URI_COMPONENT，对应第二选项，因为要兼容历史
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/23  11:23
     *
     * @param   	
     * @return  org.springframework.web.util.UriBuilderFactory
     */
    @Bean
    public UriBuilderFactory uriBuilderFactory() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://10.0.0.113");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);
        return factory;
    }

    /**
     * 这个必须自己配置，否则会报错
     * 替换默认的Http库，这里使用Apache HttpClient
     * http://hc.apache.org/httpcomponents-client-4.5.x/httpclient/examples/org/apache/http/examples/client/ClientConfiguration.java
     *
     * 这里配置失败，不知道圣杰从哪抄的代码
     *
     * 根据网上的说法，认为HttpClient 效率不如 okHttp，这里我用OK吧
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/23  11:50
     *
     * @param
     * @return  org.springframework.web.client.RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        return template;
    }
}