package com.microwu.cxd.jackson.age;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Iterator;

/**
 * Description:
 *
 * https://my.oschina.net/fangshixiang/blog/4449903
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/11   10:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JacksonDemo {

    /**
     * JsonGenerator 是抽象类
     *  WriterBasedJsonGenerator：基于 Writer 处理字符编码
     *  UTF8JsonGenerator：基于 OutputStream + UTF-8 处理字符编码
     *  默认情况下，会使用 UTF8JsonGenerator 作为实现类
     *
     *  写方法：分为写 Key 和 写 Value， key 可以独立存在，但是 value 不能独立存在
     *  推荐使用组合的方式 - writeXXXField
     *
     *  writeObject：写 POJO，必须指定一个 ObjectCodec 解码器
     *
     *  使用 Feature 去控制 JsonGenerator 的行为
     *  enable 开启操作，disable 关闭操作
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/11  11:30
     *
     * @param
     * @return  void
     */
    public static void test() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();

        // 向控制台写，这里推荐使用 try-with-resources 关闭资源
        JsonGenerator generator = jsonFactory.createGenerator(System.out, JsonEncoding.UTF8);

        try {
            // 开始写, 就是 {
            generator.writeStartObject();

            generator.writeStringField("name", "cxd");
            generator.writeNumberField("age", 23);

            // 结束写，也就是 }
            generator.writeEndObject();
        } finally {
            generator.close();
        }
    }

    public static void test02() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonGenerator generator = jsonFactory.createGenerator(System.out, JsonEncoding.UTF8)) {
            generator.writeStartObject();

            generator.writeFieldName("cxd");

            generator.writeEndObject();
        }
    }

    public static void test03() throws IOException {
        final JsonFactory jsonFactory = new JsonFactory();
        try (final JsonGenerator generator = jsonFactory.createGenerator(System.out, JsonEncoding.UTF8)) {
            generator.writeStartObject();

            generator.writeFieldName("name");
            generator.writeString("cxd");

            generator.writeFieldName("age");
            generator.writeNumber(18);

            generator.writeEndObject();
        }

    }

    public static void test04() throws IOException {
        // writeObject 重要，但是必须执行一个 ObjectCodec 解码器才能正常工作
        final JsonFactory jsonFactory = new JsonFactory();
        try (final JsonGenerator generator = jsonFactory.createGenerator(System.err, JsonEncoding.UTF8)) {
            generator.setCodec(new UserObjectCodec());
            generator.writeObject(new User());
        }
    }

    public static void main(String[] args) throws IOException {
//        test();
//        test02();
//        test03();
        test04();
    }

    public static class User {
        private String name = "cxd";
        private Integer age = 18;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static class UserObjectCodec extends ObjectCodec {
        @Override
        public Version version() {
            return null;
        }

        @Override
        public <T> T readValue(JsonParser jsonParser, Class<T> aClass) throws IOException {
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
        public void writeValue(JsonGenerator jsonGenerator, Object value) throws IOException {
            final User user = User.class.cast(value);

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", user.getName());
            jsonGenerator.writeNumberField("age", user.getAge());
            jsonGenerator.writeEndObject();
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

    public class UserTreeNode implements TreeNode {
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public JsonToken asToken() {
            return null;
        }

        @Override
        public JsonParser.NumberType numberType() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isValueNode() {
            return false;
        }

        @Override
        public boolean isContainerNode() {
            return false;
        }

        @Override
        public boolean isMissingNode() {
            return false;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public boolean isObject() {
            return false;
        }

        @Override
        public TreeNode get(String s) {
            return null;
        }

        @Override
        public TreeNode get(int i) {
            return null;
        }

        @Override
        public TreeNode path(String s) {
            return null;
        }

        @Override
        public TreeNode path(int i) {
            return null;
        }

        @Override
        public Iterator<String> fieldNames() {
            return null;
        }

        @Override
        public TreeNode at(JsonPointer jsonPointer) {
            return null;
        }

        @Override
        public TreeNode at(String s) throws IllegalArgumentException {
            return null;
        }

        @Override
        public JsonParser traverse() {
            return null;
        }

        @Override
        public JsonParser traverse(ObjectCodec objectCodec) {
            return null;
        }
    }

}