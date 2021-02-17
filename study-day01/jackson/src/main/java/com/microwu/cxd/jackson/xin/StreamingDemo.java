package com.microwu.cxd.jackson.xin;

import com.fasterxml.jackson.core.*;
import com.microwu.cxd.jackson.pojo.TwitterEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.microwu.cxd.jackson.xin.Constant.TEST_JSON_STR;
import static com.microwu.cxd.jackson.xin.Constant.TEST_OBJECT;

/**
 * Description: https://github.com/zq2599/blog_demos
 *
 * JsonParser 负责将 JSON 解析成对象的变量值，核心是循环处理 JSON 中所有的内容
 * JsonGenerator 负责将对象的变量写入 JSON 的各个属性
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/25   10:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StreamingDemo {

    /**
     * JsonFactory 线程安全
     */
    private JsonFactory jsonFactory = new JsonFactory();

    /**
     * 序列化：Json -> Object
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/25  10:23
     *
     * @param   	json
     * @return  com.microwu.cxd.jackson.pojo.TwitterEntry
     */
    public TwitterEntry deserializeJsonStr(String json) throws IOException {
        JsonParser parser = jsonFactory.createParser(json);

        if (parser.nextToken() != JsonToken.START_OBJECT) {
            parser.close();
            throw new IOException("起始位置没有大括号");
        }

        TwitterEntry result = new TwitterEntry();
        try {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();
                System.out.println("正在解析字段：" + parser.getCurrentName());

                // 解析下一个
                parser.nextToken();
                switch (fieldName) {
                    case "id":
                        result.setId(parser.getLongValue());
                        break;
                    case "text":
                        result.setText(parser.getText());
                        break;
                    case "fromUserId":
                        result.setFromUserId(parser.getIntValue());
                        break;
                    case "toUserId":
                        result.setToUserId(parser.getIntValue());
                        break;
                    case "languageCode":
                        result.setLanguageCode(parser.getText());
                        break;
                    default:
                        throw new IOException("未知字段 '" + fieldName + "'");
                }
            }
        } catch (IOException e) {
            System.out.println("反序列化出现异常");
        } finally {
            parser.close();
        }
        return result;
    }

    public String serialize(TwitterEntry twitterEntry) throws IOException {
        String rlt = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JsonGenerator generator = jsonFactory.createGenerator(byteArrayOutputStream, JsonEncoding.UTF8);

        try {
            generator.useDefaultPrettyPrinter();
            generator.writeStartObject();
            generator.writeNumberField("id", twitterEntry.getId());
            generator.writeStringField("text", twitterEntry.getText());
            generator.writeNumberField("fromUserId", twitterEntry.getFromUserId());
            generator.writeNumberField("toUserId", twitterEntry.getToUserId());
            generator.writeStringField("languageCode", twitterEntry.getLanguageCode());
            generator.writeEndObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            generator.close();
        }

        rlt = byteArrayOutputStream.toString();
        return rlt;
    }


    public static void main(String[] args) throws IOException {
        StreamingDemo demo = new StreamingDemo();

        String serialize = demo.serialize(TEST_OBJECT);
        System.out.println(serialize);

        TwitterEntry twitterEntry = demo.deserializeJsonStr(TEST_JSON_STR);
        System.out.println(twitterEntry);
    }
}