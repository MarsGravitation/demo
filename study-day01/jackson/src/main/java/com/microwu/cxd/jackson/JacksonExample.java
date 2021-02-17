package com.microwu.cxd.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microwu.cxd.jackson.pojo.Car;
import com.microwu.cxd.jackson.pojo.JacksonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Description:
 *  ObjectMapper mapper = new ObjectMapper(); // 创建一次，重用
 *  1. 入门
 *      JSON -> POJO: Car car = mapper.readValue(carJson, Car.class);
 *      POJO -> JSON: String carJson = mapper.writeValuesAsString(new Car());
 *  2. 通用集合，树模型
 *      List S: Map<String, Integer> sourceByName = mapper.readValue(jsonSource, Map.class);
 *      具有实际类型的集合：Map<String, ResultValue> results = mapper.readValue(jsonSource, new TypeReference<Map<String, ResultValue>>() { } );
 *      树模型：ObjectNode root = mapper.readTree("stuff.json"); String name = root.get("name").asText(); root.with("other").put("type", "student");
 *  3. Streaming parser, generator
 *  4. Features and Annotations:
 *      // to enable standard indentation ("pretty-printing"):
 *      mapper.enable(SerializationFeature.INDENT_OUTPUT);
 *      // to allow serialization of "empty" POJOs (no properties to serialize)
 *      // (without this setting, an exception is thrown in those cases)
 *      mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 *      // to write java.util.Date, Calendar as number (timestamp):
 *      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
 *
 *      // DeserializationFeature for changing how JSON is read as POJOs:
 *
 *      // to prevent exception when encountering unknown property:
 *      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
 *      // to allow coercion of JSON empty String ("") to null Object value:
 *      mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
 *
 * @JsonProperty 更改属性名称
 * @JsonIgnore 用于单个属性
 * @JsonIgnoreProperties 为每个类定义
 * // means that if we see "foo" or "bar" in JSON, they will be quietly skipped
 * // regardless of whether POJO has such properties
 * @JsonIgnoreProperties({ "foo", "bar" })
 * public class MyBean
 * {
 *    // will not be written as JSON; nor assigned from JSON:
 *    @JsonIgnore
 *    public String internal;
 *
 *    // no annotation, public field is read/written normally
 *    public String external;
 *
 *    @JsonIgnore
 *    public void setCode(int c) { _code = c; }
 *
 *    // note: will also be ignored because setter has annotation!
 *    public int getCode() { return _code; }
 * }
 *
 * https://github.com/FasterXML/jackson-databind
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/30   10:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JacksonExample {

    /**
     * 案例
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/30  10:15
     *
     * @param
     * @return  void
     */
    public static void test() {
        ObjectMapper objectMapper = new ObjectMapper();
        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
        try {
            // 1. json -> object
            Car car = objectMapper.readValue(carJson, Car.class);
            System.out.println(car);

            // 2. array String -> Object array
            String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";
            Car[] cars = objectMapper.readValue(jsonArray, Car[].class);
            Stream.of(cars).forEach(System.out::println);

            // 3. array String -> Object List
            List<Car> carList = objectMapper.readValue(jsonArray, new TypeReference<List<Car>>() {
            });
            System.out.println(carList);

            // 4. JSON String -> Map
            Map<String, Object> objectMap = objectMapper.readValue(carJson, new TypeReference<Map<String, Object>>() {
            });
            System.out.println(objectMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test02() {
        ObjectMapper objectMapper = new ObjectMapper();

        Car car = new Car();
        car.setBrand("BMW");
        car.setDoors(4);

        try {
            String json = objectMapper.writeValueAsString(car);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void test03() {
        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
        String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";

        Car car = JacksonUtils.string2Obj(carJson, Car.class);
        System.out.println(car);

        Car car1 = new Car();
        car1.setBrand("BMW");
        car1.setDoors(4);

        ArrayList<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car1);

        List<Car> carList = JacksonUtils.string2Obj(jsonArray, new TypeReference<List<Car>>() {
        });
        System.out.println(carList);
    }

    public static void test04() throws IOException {
        // 创建一次，重用
        ObjectMapper objectMapper = new ObjectMapper();
        // to enable standard indentation ("pretty-printing"):
        // 漂亮打印
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // to allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        // 一种功能，用于确定在找不到类型的访问器时会发生什么情况（并且没有注释指示它打算被序列化）。 如果启用（默认），则将引发异常以指示这些类型为不可序列化的类型； 如果禁用，它们将被序列化为空对象，即没有任何属性。
        // 请注意，此功能仅对那些没有任何可识别批注的“空” bean（例如@JsonSerialize ）起作用的空类型：具有批注的空类型不会导致引发异常。
        // 默认情况下启用此功能
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // to write java.util.Date, Calendar as number (timestamp):
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // DeserializationFeature for changing how JSON is read as POJOs:

        // to prevent exception when encountering unknown property:
        // 确定遇到未知属性（未映射到属性，并且没有“任何设置器”或处理程序的处理程序）的功能是否会导致失败（通过抛出JsonMappingException ）。 仅在尝试了其他所有用于未知属性的处理方法并且该属性保持未处理状态后，此设置才生效。
        // 默认情况下启用此功能（这意味着如果遇到未知属性，将抛出JsonMappingException ）。
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow coercion of JSON empty String ("") to null Object value:
        // 允许将JSON空字符串（“”）强制转换为null对象值：
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        // json -> bean
        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
        Car car = objectMapper.readValue(carJson, Car.class);
        System.out.println(car);

        // bean -> json
        Car c = new Car();
        c.setBrand("a");
        c.setDoors(1);
        String s = objectMapper.writeValueAsString(c);
        System.out.println(s);

        // simple-style POJOs
        HashMap<String, Integer> map = new HashMap<>();
        map.put("num", 1);
        String mapJson = objectMapper.writeValueAsString(map);
        System.out.println(mapJson);

        Map m = objectMapper.readValue(mapJson, Map.class);
        System.out.println(m);

        //
        String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";
        List<Car> list = objectMapper.readValue(jsonArray, new TypeReference<List<Car>>(){});
        System.out.println(list);

        String listJson = objectMapper.writeValueAsString(list);
        System.out.println(listJson);

        String treeString = "{\"name\" : \"Bob\", \"age\" : 13,\"other\" : {\"type\" : \"student\"}}";
        JsonNode jsonNode = (ObjectNode) objectMapper.readTree(treeString);
        System.out.println(jsonNode.get("name").asText());

        // 动态修改或者添加节点
        ObjectNode node = ((ObjectNode) jsonNode).with("other").put("type", "teacher");
        System.out.println(objectMapper.writeValueAsString(jsonNode));
    }

    public static void test05() throws IOException {

        String jsonStr = "{\"name\":\"YourBatman\",\"age\":18}";

        JsonNode node = new ObjectMapper().readTree(jsonStr);

        System.out.println("-------------向结构里动态添加节点------------");
        // 动态添加一个myDiy节点，并且该节点还是ObjectNode节点
        ((ObjectNode) node).with("myDiy").put("contry", "China");

        System.out.println(node);
    }

    public static void main(String[] args) throws IOException {
//        test();
//        test02();
//        test03();
//        test04();
        test05();
    }
}