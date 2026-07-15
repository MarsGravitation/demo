package com.microwu.cxd.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.microwu.cxd.utils.DateFormatterUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Description:
 *
 * https://juejin.cn/post/6844904177479450632
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/18   10:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class GlobalDateTimeConfig {

    /**
     * LocalDateTime 转换器。用于转换 RequestParam 和 PathVariable 参数
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2022/5/18 10:33
     *
     * @param:
     * @return: org.springframework.core.convert.converter.Converter<java.lang.Long,java.time.LocalDateTime>
     */
    @Bean
    public Converter<String, LocalDateTime> timeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return DateFormatterUtils.SINGLETON.longToTime(Long.valueOf(source));
            }
        };
    }

    /**
     * json 序列化和反序列化转换器
     *
     * 用于转换 Post 请求体中的 json
     * 以及将我们的对象序列化为返回响应的 json
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2022/5/18 10:37
     *
     * @param:
     * @return: org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializerCustomizer())
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializerCustomizer());
    }

    /**
     * LocalDatetime -> Long
     */
    static class LocalDateTimeSerializerCustomizer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(DateFormatterUtils.SINGLETON.timeToLong(localDateTime));
        }
    }

    /**
     * Long -> LocalDatetime
     */
    static class LocalDateTimeDeserializerCustomizer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return DateFormatterUtils.SINGLETON.longToTime(jsonParser.getLongValue());
        }
    }

}
