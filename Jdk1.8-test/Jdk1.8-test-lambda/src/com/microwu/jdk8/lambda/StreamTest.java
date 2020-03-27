package com.microwu.jdk8.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:     String 并不是一个容器, 它只是对容器的功能进行了增强, 添加了很多便利的操作
 *      并且有并行, 串行两种执行模式, 并行模式充分的利用了多核处理器的优势, 使用fork/join框架
 *      进行了任务拆分, 同时提高了执行速度. 简而言之, Stream 就是提供了一种搞笑且易于使用的处理数据的方式
 *
 * 特点:
 *      1. Stream 自己不会存储元素
 *      2. Stream 的操作不会改变源对象.相反, 它们会返回一个持有结果的新Stream
 *      3. Stream 操作是延迟执行的. 它会等到需要结果的时候才执行. 也就是执行中断操作的时候
 *
 * 创建Stream
 *      由集合创建: Java8 中的Collectin 接口被扩展, 提供了两个获取流的方法, 这两个方法时default,
 *                  也就是说所有实现Collectin接口的接口都不需要实现就可以直接使用
 *       1. default Stream stream - 返回一个顺序流
 *       2. default Stream parallelStream - 返回一个并行流
 *
 *       由数组创建: Java8中 Arrays 的静态方法 stream() 可以获取数据流
 *       1. static Stream stream(T[] array): 返回一个流
 *       2. 重载形式, 能够处理对应基本类型的数组
 *
 * Stream 的中间操作
 *      如果Stream 只有中间操作是不会执行的, 当执行终端操作的时候才会执行中间操作, 这种方式称为延迟加载或惰性求值
 *      多个中间操作组成一个中间操作链, 只有当执行终端操作的时候才会执行一遍中间操作链
 *      distinct - 去重
 *
 *
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/13   11:48
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StreamTest {

    private static List<User> list() {
        ArrayList<User> users = new ArrayList<>(5);
        users.add(new User(12, "A"));
        users.add(new User(25, "B"));
        users.add(new User(21, "C"));
        return users;
    }

    /**
     * 去重
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  13:50
     *
     * @param
     * @return  void
     */
    private static void test01() {
        // 三个年轻人A, B, C报名了一次活动
        List<String> users = new ArrayList<>(3);
        users.add("A");
        users.add("B");
        users.add("C");
        users.add("A");
        // 由于A 多报名了一次, 现在需要去重
        users.stream().distinct().forEach(System.out::println);

    }

    /**
     * 过滤
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  13:56
     *
     * @param
     * @return  void
     */
    private static void test02() {
        List<User> list = list();
        list.stream().filter(user -> user.getAge() > 20).forEach(user -> System.out.println(user.getName()));
    }

    /**
     * 截断流
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  13:59
     *
     * @param
     * @return  void
     */
    public static void test03() {
        List<User> list = list();
        // 按年龄排序, 倒排需要用到reversed方法, 然后limit取出第一个, 看谁的年龄最大
        list.stream().sorted(Comparator.comparing(User::getAge).reversed())
                .limit(1)
                .forEach(user -> System.out.println(user));
    }

    /**
     * map 接受一个Function函数作为参数, 并映射成一个新元素
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  14:07
     *
     * @param
     * @return  void
     */
    public static void test04() {
        List<User> list = list();
        list.stream().map(user -> "欢迎: " + user.getName()).forEach(System.out::println);

    }

    /**
     * flatMap 接受一个Function函数作为参数, 将流中的每个值都转换成一个流, 然后把所有的流连接成一个流
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  14:09
     *
     * @param
     * @return  void
     */
    private static void test05() {

    }

    /**
     * 终端操作
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  14:54
     *
     * @param
     * @return  void
     */
    public static void test06() {
        // foreach
        // collect
        // 收集名称到List
        List<User> list = list();
        List<String> collect = list.stream().map(user -> user.getName()).collect(Collectors.toList());
        collect.stream().forEach(System.out::println);
        // 收集到map, 名字作为key, user对象作为value
        Map<String, User> userMap = list.stream()
                .collect(Collectors.toMap(User::getName, Function.identity(), (k1, k2) -> k2));
        for(Map.Entry<String, User> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        test06();
    }


    static class User {
        private int age;
        private String name;

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}