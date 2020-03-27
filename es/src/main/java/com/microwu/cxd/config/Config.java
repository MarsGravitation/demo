package com.microwu.cxd.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/8   16:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class Config extends AbstractElasticsearchConfiguration {

    /**
     * 高级Rest 客户端
     *
     * Java高级客户端现在是ES 的默认客户端. 异步调用在客户端托管线程池中进行操作,
     * 并且在请求完成时需要通知回调
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/8  16:49
     *
     * @param
     * @return  org.elasticsearch.client.RestHighLevelClient
     */
    @Bean
    RestHighLevelClient client() {
        // 使用构建器来提供集群地址
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("192.168.133.134:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();

    }

    /**
     * extends AbstractElasticsearchConfiguration
     *
     * 如果继承 AbstractElasticsearchConfiguration, 则真正获取连接的是重写的方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/8  16:43
     *
     * @param
     * @return  org.elasticsearch.client.RestHighLevelClient
     */
    @Override
    public RestHighLevelClient elasticsearchClient() {
        return RestClients.create(ClientConfiguration.create("192.168.133.134:9200")).rest();
    }

    /**
     * 1. 覆盖默认的EntityMapper
     * 2. 使用提供的SIMM 避免不一致, 并提供用于转换器注册的通用转换服务
     * 3. 如果适用, 可选择设置自定义转换
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/8  16:51
     *
     * @param
     * @return  org.springframework.data.elasticsearch.core.EntityMapper
     */
    @Bean
    @Override
    public EntityMapper entityMapper() {
        ElasticsearchEntityMapper elasticsearchEntityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(), new DefaultConversionService());
        elasticsearchEntityMapper.setConversions(elasticsearchCustomConversions());
        return elasticsearchEntityMapper;
    }

//    @Bean
//    @Override
//    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
//        return new ElasticsearchCustomConversions(Arrays.asList(new AddressToMap(), new MapToAddress()));
//    }
//
//    static class AddressToMap implements Converter<Person, Map<String, Object>> {
//
//        @Override
//        public Map<String, Object> convert(Person person) {
//            LinkedHashMap<String, Object> target = new LinkedHashMap<>();
//            target.put("name", person.getUsername());
//            return target;
//        }
//
//        @Override
//        public JavaType getInputType(TypeFactory typeFactory) {
//            return null;
//        }
//
//        @Override
//        public JavaType getOutputType(TypeFactory typeFactory) {
//            return null;
//        }
//    }
//
//    @ReadingConverter
//    static class MapToAddress implements Converter<Map<String, Object>, Person> {
//        @Override
//        public Person convert(Map<String, Object> source) {
//            Person person = new Person();
//            return person;
//        }
//
//        @Override
//        public JavaType getInputType(TypeFactory typeFactory) {
//            return null;
//        }
//
//        @Override
//        public JavaType getOutputType(TypeFactory typeFactory) {
//            return null;
//        }
//    }
}