package com.microwu.cxd.jackson.xin;

/**
 * Description: SpringBoot 整合
 *  1. 配置文件
 *      spring:
 *   jackson:
 *     # 日期格式化
 *     date-format: yyyy-MM-dd HH:mm:ss
 *     # 序列化相关
 *     serialization:
 *       # 格式化输出
 *       indent_output: true
 *       # 忽略无法转换的对象
 *       fail_on_empty_beans: true
 *     # 反序列化相关
 *     deserialization:
 *       # 解析json时，遇到不存在的属性就忽略
 *       fail_on_unknown_properties: false
 *     # 设置空如何序列化
 *     defaultPropertyInclusion: NON_EMPTY
 *     parser:
 *       # 允许特殊和转义符
 *       allow_unquoted_control_chars: true
 *       # 允许单引号
 *       allow_single_quotes: true
 *
 *  2. 配置文件
 *      @Configuration
 * public class JacksonConfig {
 *
 *     @Bean("customizeObjectMapper")
 *     @Primary
 *     @ConditionalOnMissingBean(ObjectMapper.class)
 *     public ObjectMapper getObjectMapper(Jackson2ObjectMapperBuilder builder) {
 *         ObjectMapper mapper = builder.build();
 *
 *         // 日期格式
 *         mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
 *
 *         // 美化输出
 *         mapper.enable(SerializationFeature.INDENT_OUTPUT);
 *
 *         return mapper;
 *     }
 * }
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/25   13:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SpringBootJackson {
}