package com.microwu.jdk8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/26   14:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StreamTest02 {

    private static List<String> list = new ArrayList<>();

    static {
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
    }

    /**
     * 创建Stream
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/26  14:29
     *
     * @param
     * @return  void
     */
    private static void test() {
        String[] arr = new String[]{"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        stream = Stream.of(arr);
        stream.forEach(System.out::println);

        ArrayList<String> list = new ArrayList<>();
        list.stream();
    }

    /**
     * Stream 的基本操作
     *  count - 计数
     *  matching - 匹配
     *  filtering - 过滤
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/26  14:34
     *
     * @param
     * @return  void
     */
    private static void test02() {
        long count = list.stream().distinct().count();
        System.out.println(count);

        System.out.println("list 是否存在一个a元素：" + list.stream().anyMatch(e -> e.equalsIgnoreCase("a")));
        System.out.println("list 是否全是a元素：" + list.stream().allMatch(e -> e.equalsIgnoreCase("a")));

        // 过滤出 filter 为 true 的数据
        list.stream().filter(e -> e.equalsIgnoreCase("b")).forEach(System.out::println);

        // map 就是对每个值进行加工，然后作为新的Stream 返回
        list.stream().map(e -> "元素：" + e).forEach(System.out::println);

        // flatMap

        // reduction
        // 两个参数，第一个是初始值，第二个是累加的函数
        Integer reduce = Arrays.asList(1, 1, 1).stream().reduce(100, (a, b) -> a + b);
        System.out.println("叠加之后的值：" + reduce);

        // collection 将 stream 转换成集合
        list.stream().collect(Collectors.toList());
    }

    /**
     * map 和 flatMap 的区别
     *  通过一个案例感受一下，将字符串数组转换成字符数组，并去重
     *  https://blog.csdn.net/Mark_Chao/article/details/80810030
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/26  14:50
     *
     * @param
     * @return  void
     */
    private static void test03() {
        String[] words = new String[]{"Hello", "World"};
        // 1. map
        Stream.of(words).map(s -> s.split("")).distinct().forEach(System.out::println);

        // 2. flatMap
        // 将多个流扁平化为一个流
        Stream.of(words).map(s -> s.split("")).flatMap(Arrays::stream).distinct().forEach(System.out::println);

    }

    public static void main(String[] args) {
        test03();
    }


}