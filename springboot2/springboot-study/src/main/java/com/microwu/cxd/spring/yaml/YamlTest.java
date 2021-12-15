package com.microwu.cxd.spring.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 3. 入口点
 *  Yaml yaml = new Yaml(); // 线程不安全
 *
 * 4. 加载 yaml 文档
 *  SnakeYAML 支持从 string 或 InputStream 加载文档
 *
 * https://www.cnblogs.com/xiaoqi/p/SnakeYAML.html
 *
 * YAML 格式：
 *  - yaml中还支持流式（flow）语法表示对象
 *
 *  key:
 *      child-key: value
 *      child-key2: value2
 *
 *
 *  key: {child-key: value, child-key2: value2}
 *
 *  - 引用 === & 完成锚点定义，* 锚点引用
 *  hr:
 *  - Mark McGwire
 *  - &SS Sammy Sosa
 *  rbi:
 *  - *SS
 *  - Ken Griffey
 *
 *  rbi:
 *  - Sammy Sosa
 *  - Ken Griffey
 *  hr:
 *  - Mark McGwire
 *  - Sammy Sosa
 *
 *  - 合并
 *
 *  merge:
 *  - &CENTER {x: 1, y: 2}
 *
 *  Sample1:
 *  <<: *CENTER
 *  r: 10
 *
 *  sample1 = {r = 10. y = 2, x = 1}
 *
 * https://www.cnblogs.com/caibao666/p/10238497.html
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/9/22  13:33
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class YamlTest {

    private static final String CUSTOMER = "firstName: \"John\"\n" +
            "lastName: \"Doe\"\n" +
            "age: 20";

    private static final String CUSTOMER2 = "firstName: \"John\"\n" +
            "lastName: \"Doe\"\n" +
            "age: 31\n" +
            "contactDetails:\n" +
            "   - type: \"mobile\"\n" +
            "     number: 123456789\n" +
            "   - type: \"landline\"\n" +
            "     number: 456786868\n" +
            "homeAddress:\n" +
            "   line: \"Xyz, DEF Street\"\n" +
            "   city: \"City Y\"\n" +
            "   state: \"State Y\"\n" +
            "   zip: 345657";

    private static final String WORKER_CONF = "output.console: {enabled: true}\n" +
            "filebeat.inputs:\n" +
            "- type: log\n" +
            "  paths: [/opt/test/log/test.log, /opt/test/log/test2.log, /opt/test/log/test3.log]\n" +
            "  encoding: gbk\n" +
            "  scan_frequency: 2s\n" +
            "  harvester_buffer_size: 16384\n" +
            "  max_bytes: 10485760\n" +
            "  is_eof: false\n" +
            "  is_read_all: false\n" +
            "  exclude_lines: '[^DBG]'\n" +
            "  include_lines: '[^ERR, ^WARN]'\n" +
            "groups:\n" +
            "  fields: &id001 {id: '890256035326070784'}\n" +
            "  group: '33'\n" +
            "fields: *id001";

    /**
     * 基本用法
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/9/22     13:37
     *
     * @param
     * @return void
     */
    public static void test() throws IOException {
        Yaml yaml = new Yaml();
//        InputStream inputStream = YamlTest.class.getResourceAsStream("customer.yaml");
        // 默认情况下，load 方法返回一个 map 对象
        Object load = yaml.load(WORKER_CONF);
        System.out.println(load);

        System.out.println(yaml.dump(load));
    }

    public static void test02() {
        Yaml yaml = new Yaml(new Constructor(Customer.class));
        Customer customer = yaml.load(CUSTOMER);
        System.out.println(customer);
    }

    /**
     * 嵌套对象
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/9/22     13:58
     *
     * @param
     * @return void
     */
    public static void test03() {
        Yaml yaml = new Yaml(new Constructor(Customer.class));
        Object load = yaml.load(CUSTOMER2);
        System.out.println(load);
    }

    /**
     * 生成 YAML 文件
     *
     * 支持将 Java 对象序列化为 yml
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/9/22     14:03
     *
     * @param
     * @return void
     */
    public static void test05() {
        // 从 map -> yaml
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("name", "Silenthand Olleander");
        data.put("race", "Human");
        data.put("traits", new String[] { "ONE_HAND", "ONE_EYE" });
        Yaml yaml = new Yaml();
//        StringWriter writer = new StringWriter();
//        yaml.dump(data, writer);
//        System.out.println(writer.toString());
        System.out.println(yaml.dumpAs(data, Tag.YAML, DumperOptions.FlowStyle.BLOCK));
    }

    /**
     * java -> yaml
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/9/22     14:05
     *
     * @param
     * @return void
     */
    public static void test06() {
        Customer customer = new Customer();
        customer.setAge(45);
        customer.setFirstName("Greg");
        customer.setLastName("McDowell");
        ArrayList<Contact> contacts = new ArrayList<>();
        customer.setContactDetails(contacts);
        Contact contact = new Contact();
        contact.setType("mobile");
        contact.setNumber(123456789);
        contacts.add(contact);
        Contact contact1 = new Contact();
        contact1.setType("landline");
        contact1.setNumber(456789);
        contacts.add(contact1);
        Yaml yaml = new Yaml();
//        StringWriter writer = new StringWriter();
        // !!com.microwu.cxd.spring.yaml.Customer {age: 45, contactDetails: null, firstName: Greg,
        //  homeAddress: null, lastName: McDowell}
        // 包含标签名
//        yaml.dump(customer, writer);
        String s = yaml.dumpAs(customer, Tag.YAML, DumperOptions.FlowStyle.BLOCK);
        System.out.println(s);
    }

    public static void main(String[] args) throws IOException {
        test();
    }

}
