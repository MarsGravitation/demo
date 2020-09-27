package com.microwu.cxd.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.jackson.pojo.Car;
import com.microwu.cxd.jackson.pojo.JacksonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Description:
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

    public static void main(String[] args) {
//        test();
//        test02();
        test03();
    }
}