package com.microwu.jdk8.lambda;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Description:     Lambda 分为三部分: 参数列表, 操作符, Lambda体
 *      1. 可选类型声明: 不需要声明参数类型, 编译器可以统一识别参数值. 也就是说
 *      (s) -> System.out.println(s) 和 (String s) -> System.out.println(s)是一样的
 *      2. 可选的参数圆括号: 一个参数无需定义圆括号
 *      3. 可选的大括号
 *      4. 可选的返回关键字
 *
 * 方法引用:
 *      方法引用是用来的直接访问类或者实例已经存在的方法或者构造方法, 提供了一种引用而
 *      不执行方法的方式. 是一种更简洁更易懂的Lambda表达式, 当Lambda表达式中只是执行一个方法调用时,
 *      直接使用方法引用的形式可读性更高一些.
 *      方法引用使用 :: 操作符来表示, 坐标是类名或者实例名, 右边是方法名
 *
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/18   15:50
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LambdaTest {
    public static void main(String[] args) {
        test06();
    }

    private void test() {
        // 传统写法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello lambda");
            }
        }).start();

        // lambda
        new Thread(() -> System.out.println("hello lambda")).start();
    }

    private void test02() {
        ThreadFactory threadFactory = r -> new Thread();
        threadFactory.newThread(() -> {

        });
    }

    private static void test03() {
        // 消费者接口
        Consumer consumer = System.out::println;
        consumer.accept("hello function");

        Consumer<String> consumer1 = s -> System.out.print("车名: " + s.split(",")[0]);
        Consumer<String> consumer2 = s -> System.out.println("--> 颜色: " + s.split(",")[1]);

        String[] strings = {"保时捷, 白色", "法拉利, 红色"};
        for(String string : strings) {
            // 默认方法andThen, 先消费然后再消费
            // 先执行调用andThen接口的accept方法,然后在执行andThen方法参数after中的accept方法
            consumer1.andThen(consumer2).accept(string);
        }
    }

    private static void test04() {
        // 供给接口
        Supplier<String> supplier = () -> "我要变得很有钱";
        System.out.println(supplier.get());
    }

    private static void test05() {
        // 函数型接口
        Function<Integer, Integer> function = e -> e * 6;
        System.out.println(function.apply(2));

        // 默认方法: compose
        // 先执行compose方法参数before中的apply
        // 然后将执行结果传递给调用的compose函数的中的apply执行
        Function<Integer, Integer> function1 = e -> e * 2;
        Function<Integer, Integer> function2 = e -> e + 6;

        Integer apply = function1.compose(function2).apply(3);
        System.out.println(apply);

        // andThen
        Integer apply1 = function1.andThen(function2).apply(3);
        System.out.println(apply1);
    }

    private static void test06() {
        // 断言型接口
        Predicate<Integer> predicate = i -> i > 20;
        System.out.println(predicate.test(10));

        // 默认方法 and
        Predicate<String> predicate1 = s -> s.isEmpty();
        Predicate<String> predicate2 = Objects::nonNull;
        boolean test = predicate1.and(predicate2).test("测试");
        System.out.println(test);

    }

}