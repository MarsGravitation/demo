package com.microwu.cxd.jackson.xin;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.Date;

/**
 * Description: 属性注解
 *  JsonProperty: 作用在成员变量和方法上，在序列化和反序列化操作中指定 json 的名称
 *  JsonIgnore: 不参与序列化和反序列化操作
 *  JacksonInject: 反序列化时，将配制好的值注入被 JacksonInject 注解的字段
 *  JsonSerialize: 序列化，使用 using 属性指定序列化操作的类
 *  JsonDeserialize: 反序列化
 *  JsonRawValue: 序列化的结果是原始值
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/25   11:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FieldAnnotation {

    @JsonProperty(value = "json_field", index = 0)
    private String field;

    @JsonIgnore
    private String field2;

    @JacksonInject(value = "defaultField")
    private String field3;

    @JsonSerialize(using = Date2LongSerialize.class)
    private Date field5;

    @JsonDeserialize(using = Long2DateDeserialize.class)
    private Date field4;

    static class Date2LongSerialize extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.getTime());
        }
    }

    static class Long2DateDeserialize extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p != null && ctxt != null && p.getLongValue() > 0L) {
                return new Date(p.getLongValue());
            }
            return null;
        }
    }

}