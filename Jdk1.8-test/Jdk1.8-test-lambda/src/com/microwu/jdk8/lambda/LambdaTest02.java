package com.microwu.jdk8.lambda;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/4/18   15:26
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */
public class LambdaTest02 {

    /**
     * 使用stream 实现 if/else
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/26  15:05
     */
    private static void test() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 以前的形式
        integers.stream().forEach(i -> {
            if (i % 2 == 0) {
                System.out.println("我是偶数！！！");
            } else {
                System.out.println("我是基数！！！");
            }
        });

        // 另一种方案，使用过滤
        integers.stream().filter(integer -> integer % 2 == 0).forEach(integer -> System.out.println("偶数"));
    }

    /**
     * map 使用 stream
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/4/26  15:11
     */
    private static void test02() {
        HashMap<String, String> map = new HashMap<>();
        map.put("cxd", "25");
        map.put("cxj", "26");

        // 查询 age = 25 的 key
        String key = map.entrySet().stream().filter(entry ->
                entry.getValue().equalsIgnoreCase("25")).findFirst().get().getKey();

        String s = map.entrySet().stream().filter(entry -> "25".equals(entry.getValue())).map(Map.Entry::getValue).findFirst().get();

        System.out.println(key + ":" + s);
    }

    /**
     * 方法引用
     *  1. Lambda体中调用方法的参数列表与返回值类型，要与函数式接口中的抽象方法和返回值类型一致
     *  2. 若Lambda 参数列表中的第一个参数是实例方法的调用者，第二个参数是实例方法的参数时，
     *      可以使用ClassName::method
     *
     * 几种形式：
     *  类::静态方法
     *  类::实例方法
     *  对象::实例方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/26  15:35
     *
     * @param
     * @return  void
     */
    private static void test03() {
        // Consumer<String> consumer = (s) -> System.out.println(s);
        Consumer<String> consumer = System.out::println;

        // Function<String, Integer> stringToInteger = (String s) -> Integer.parseInt(s);
        Function<String, Integer> function = Integer::parseInt;

        // 类::实例方法
        // BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
        BiPredicate<List<String>, String> contains = List::contains;

        // Predicate predicate = Object::equals; - 编译不通过
        // 我认为是 Predicate 只接受一个参数，不符合类::实例的调用规则
        // 这个要求比较苛刻
    }

    /**
     * 常用的函数式接口
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/26  16:02
     *
     * @param
     * @return  void
     */
    private static void test04() {
        // 1.消费型接口 - 接受一个参数，没有返回值
        Consumer consumer = System.out::println;

        // 2. 供给型接口： 无参数，有返回值
        Supplier supplier = () -> "a";

        // 3. 函数型接口，传入一个参数，返回想要的结果
        Function<Integer, Integer> function = e -> e * 2;

        // 4. 断言型接口，传入一个参数，返回一个布尔值
        Predicate<Integer> predicate = t -> t > 0;
    }

    public static void main(String[] args) {
        test();
    }
}