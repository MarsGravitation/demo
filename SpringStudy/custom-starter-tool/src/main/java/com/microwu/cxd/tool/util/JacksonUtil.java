package com.microwu.cxd.tool.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/18   10:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JacksonUtil {
    /**
     * 保存全局唯一实例，这个 objectMapper 是可以复用的
     */
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 忽略多余属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * String -> Object
     * 处理简单的 bean 风格的 POJO，也可以处理 List S，Map S（即类型是 String，Number，Boolean）
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/30  16:17
     *
     * @param   	json
     * @param 		clazz
     * @return  T
     */
    public static <T> T string2Obj(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T)json : objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反序列化的时候需要指定类型 - 类型擦除
     *
     * 如果具有 POJO 值，则需要指定实际类型
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/30  16:22
     *
     * @param   	json
     * @param 		typeReference
     * @return  T
     */
    public static <T> T string2Obj(String json, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(json) || typeReference == null) {
            return null;
        }

        try {
            return (T) (typeReference.getType().equals(String.class) ? json : objectMapper.readValue(json, typeReference));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * object -> string
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/30  16:26
     *
     * @param   	t
     * @return  java.lang.String
     */
    public static <T> String obj2String(T t) {
        if (t == null) {
            return null;
        }
        try {
            return t instanceof String ? (String) t : objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}