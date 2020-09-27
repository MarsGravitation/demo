package com.microwu.cxd.jackson.age;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import java.io.IOException;
import java.util.Iterator;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/11   10:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JacksonDemo02 {

    public static void test() throws IOException {
        String json =  "{\"name\":\"YourBatman\",\"age\":18}";
        final Person person = new Person();

        final JsonFactory jsonFactory = new JsonFactory();
        try (final JsonParser parser = jsonFactory.createParser(json)) {
            // 只要还没有结束
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                final String fieldName = parser.getCurrentName();
                if ("name".equalsIgnoreCase(fieldName)) {
                    parser.nextToken();
                    person.setName(parser.getText());
                } else if ("age".equalsIgnoreCase(fieldName)) {
                    parser.nextToken();
                    person.setAge(parser.getIntValue());
                }
            }
            System.out.println(person);
        }

    }

    /**
     * 自动绑定，实际上是委托给了 ObjectCodec 去实现
     *
     * JsonParser 的 Feature
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/11  13:41
     *
     * @param
     * @return  void
     */
    public static void test02() throws IOException {
        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18, \"pickName\":null}";

        final JsonFactory jsonFactory = new JsonFactory();
        try (final JsonParser parser = jsonFactory.createParser(jsonStr)) {
            parser.setCodec(new PersonObjectCodec());

            System.out.println(parser.readValueAs(Person.class));
        }
    }

    /**
     * JsonToken
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/11  13:49
     *
     * @param
     * @return  void
     */
    public static void test03() throws IOException {
        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18, \"pickName\":null}";
        final JsonFactory jsonFactory = new JsonFactory();

        try (final JsonParser parser = jsonFactory.createParser(jsonStr)) {
            JsonToken token;
            while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
                System.out.println(token + " -> " + parser.getValueAsString());
            }
        }
    }

    /**
     * JsonFactory 用于配置和构建 JsonGenerator 和 JsonParser，这个工厂实例是线程安全的，因此可以重复使用
     *
     * 创建 JsonGenerator 实例，它负责向目的地写数据，强调的是 目的地在哪，如何写
     *  - 默认字符集为 u8，使用 UTF8JsonGenerator 作为实现类
     *
     * 创建 JsonParser 实例，它负责从一个 Json 字符串中提取出值，强调的是 数据从哪来，如何解析
     *
     * JsonFactory 的 Feature
     *  对读/写生效 - enable、disable、configure
     *  务必在 factory.createXXX() 之前配置好对应的 Feature 特征
     *
     * JsonFactoryBuilder
     *  推荐使用 builder 模式构建实例
     *
     * SPI 方式
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/11  13:55
     *
     * @param
     * @return  void
     */
    public static void test04() {

    }

    public static void main(String[] args) throws IOException {
//        test();
//        test02();
        test03();
    }

    @Data
    public static class Person {
        private String name;
        private Integer age;
    }

    public static class PersonObjectCodec extends ObjectCodec {

        @Override
        public Version version() {
            return null;
        }

        @Override
        public <T> T readValue(JsonParser jsonParser, Class<T> value) throws IOException {
            try {
                Person person = (Person) value.newInstance();

                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    final String fieldName = jsonParser.getCurrentName();
                    if ("name".equals(fieldName)) {
                        jsonParser.nextToken();
                        person.setName(jsonParser.getText());
                    } else if ("age".equals(fieldName)) {
                        jsonParser.nextToken();
                        person.setAge(jsonParser.getIntValue());
                    }
                }

                return (T) person;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public <T> T readValue(JsonParser jsonParser, TypeReference<?> typeReference) throws IOException {
            return null;
        }

        @Override
        public <T> T readValue(JsonParser jsonParser, ResolvedType resolvedType) throws IOException {
            return null;
        }

        @Override
        public <T> Iterator<T> readValues(JsonParser jsonParser, Class<T> aClass) throws IOException {
            return null;
        }

        @Override
        public <T> Iterator<T> readValues(JsonParser jsonParser, TypeReference<?> typeReference) throws IOException {
            return null;
        }

        @Override
        public <T> Iterator<T> readValues(JsonParser jsonParser, ResolvedType resolvedType) throws IOException {
            return null;
        }

        @Override
        public void writeValue(JsonGenerator jsonGenerator, Object o) throws IOException {

        }

        @Override
        public <T extends TreeNode> T readTree(JsonParser jsonParser) throws IOException {
            return null;
        }

        @Override
        public void writeTree(JsonGenerator jsonGenerator, TreeNode treeNode) throws IOException {

        }

        @Override
        public TreeNode createObjectNode() {
            return null;
        }

        @Override
        public TreeNode createArrayNode() {
            return null;
        }

        @Override
        public JsonParser treeAsTokens(TreeNode treeNode) {
            return null;
        }

        @Override
        public <T> T treeToValue(TreeNode treeNode, Class<T> aClass) throws JsonProcessingException {
            return null;
        }
    }
}