package com.microwu.cxd.jackson.pojo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description:
 *
 * https://developer.aliyun.com/article/636124
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/30   10:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JacksonUtils {

    /**
     * 保存全局的唯一实例，这个 objectMapper 是可以复用的
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * string -> object
     * 可以处理简单的 bean 风格的 POJO， 也可以处理 List S、Map S, Object types (Strings, Numbers, Booleans)
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/30  10:37
     *
     * @param   	json
     * @param 		clazz
     * @return  T
     */
    public static <T> T string2Obj(String json, Class<T> clazz) {
        if (json == null || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * object -> string
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/30  10:41
     *
     * @param   	obj
     * @return  java.lang.String
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反序列化的时候需要指定类型 - 类型擦除
     *
     * 如果具有 POJO 值，则需要指示实际类型
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/30  10:47
     *
     * @param   	json
     * @param 		typeReference
     * @return  T
     */
    public static <T> T string2Obj(String json, TypeReference<T> typeReference) {
        if (json == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? json : objectMapper.readValue(json, typeReference));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> T string2Object(String json, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}