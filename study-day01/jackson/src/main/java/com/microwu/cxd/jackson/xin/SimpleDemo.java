package com.microwu.cxd.jackson.xin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.microwu.cxd.jackson.pojo.TwitterEntry;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microwu.cxd.jackson.xin.Constant.TEST_JSON_STR;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/25   10:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SimpleDemo {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // 常用配置

        // 序列化结果格式化
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 空对象不要抛异常
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 时间格式序列化成时间戳
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 反序列化时，遇到未知属性不要抛出异常
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 反序列化时，空字符串对于的实例属性为 null
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 允许字段名没有引号，可以减小 json 体积
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);


        // 对象 -> 字符串
        TwitterEntry twitterEntry = new TwitterEntry();
        twitterEntry.setId(123456L);
        twitterEntry.setFromUserId(101);
        twitterEntry.setToUserId(102);
        twitterEntry.setText("this is a message for serializer test");
        twitterEntry.setLanguageCode("zh");

        System.out.println("序列化的字符串：" + mapper.writeValueAsString(twitterEntry));

        // 字符串 -> 对象
        TwitterEntry tFromStr = mapper.readValue(TEST_JSON_STR, TwitterEntry.class);
        System.out.println(tFromStr);

        // 集合序列化操作
        Map<String, Object> map = new HashMap<>();
        map.put("name", "tom");
        map.put("age", 11);

        Map<String, String> addr = new HashMap<>();
        addr.put("city","深圳");
        addr.put("street", "粤海");

        map.put("addr", addr);

        String mapJsonStr = mapper.writeValueAsString(map);
        System.out.println(mapJsonStr);

        Map<String, Object> mapFromStr = mapper.readValue(mapJsonStr, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(mapFromStr);

        // JsonNode 操作
        JsonNode jsonNode = mapper.readTree(mapJsonStr);
        String name = jsonNode.get("name").asText();
        int age = jsonNode.get("age").asInt();
        String city = jsonNode.get("addr").get("city").asText();
        String street = jsonNode.get("addr").get("street").asText();
        System.out.println(name + "," + age + "," + city + "," + street);

        // 时间类型格式
//        Map<String, Object> dateMap = new HashMap<>();
//        dateMap.put("")

        // json 数组
        String jsonArrayStr = "[{\n" +
                "  \"id\":1,\n" +
                "  \"text\":\"text1\",\n" +
                "  \"fromUserId\":11, \n" +
                "  \"toUserId\":111,\n" +
                "  \"languageCode\":\"en\"\n" +
                "},\n" +
                "{\n" +
                "  \"id\":2,\n" +
                "  \"text\":\"text2\",\n" +
                "  \"fromUserId\":22, \n" +
                "  \"toUserId\":222,\n" +
                "  \"languageCode\":\"zh\"\n" +
                "},\n" +
                "{\n" +
                "  \"id\":3,\n" +
                "  \"text\":\"text3\",\n" +
                "  \"fromUserId\":33, \n" +
                "  \"toUserId\":333,\n" +
                "  \"languageCode\":\"en\"\n" +
                "}]";

        // json 数组 -> 对象数组
        TwitterEntry[] twitterEntries = mapper.readValue(jsonArrayStr, TwitterEntry[].class);
        System.out.println(Arrays.toString(twitterEntries));

        // json 数组 -> 对象集合
        List<TwitterEntry> twitterEntryList = mapper.readValue(jsonArrayStr, new TypeReference<List<TwitterEntry>>() {
        });
        System.out.println(twitterEntryList);
    }

}