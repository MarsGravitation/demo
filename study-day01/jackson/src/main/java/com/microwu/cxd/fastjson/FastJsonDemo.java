package com.microwu.cxd.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.microwu.cxd.jackson.pojo.Car;

import java.lang.reflect.Type;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/7/22  14:51
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class FastJsonDemo {

    /**
     * Hello World
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/7/22     15:00
     *
     * @param
     * @return void
     */
    public static void test() {
        // JavaBean -> String
        Car car = new Car();
        car.setBrand("Mercedes");
        car.setDoors(5);

        String carJson = JSON.toJSONString(car);
        System.out.println(carJson);

        // String -> JavaBean
        Car car1 = JSON.parseObject(carJson, Car.class);
        System.out.println(car1);
    }

    /**
     * API
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/7/22     15:14
     *
     * @param
     * @return void
     */
    public static void test02() {
        // parse Tree
        String jsonStr = "{}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        // parse POJO
        Car car = JSON.parseObject(jsonStr, Car.class);

        // parse POJO Generic
        Type type = new TypeReference<Car>() {
        }.getType();
        Car car1  = JSON.parseObject(jsonStr, type);

        // POJO to json string
        String s = JSON.toJSONString(car);

    }

    public static void main(String[] args) {
        test();
    }

}
