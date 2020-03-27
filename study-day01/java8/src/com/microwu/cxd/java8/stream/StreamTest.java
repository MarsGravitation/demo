package com.microwu.cxd.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:     Java8添加了一个新的抽象类型为流Stream，可以让你以一种声明的方式处理数据
 *      Stream是一种对Java集合运算和表达的高阶抽象
 *      这种风格将 要处理的元素集合看作一种流，流在管道中传输，并且可以在管道的节点上进行处理
 *      +--------------------+       +------+   +------+   +---+   +-------+
 *      | stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
 *      +--------------------+       +------+   +------+   +---+   +-------+
 *
 *      串行流
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/4/26   11:45
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StreamTest {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        // 计算空字符串的数量
        long count = strings.stream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串的数量：" + count);

        // 字符串长度为3的数量
        count = strings.stream().filter(string -> string.length() == 3).count();
        System.out.println("字符串长度为3的数量：" + count);

        // 筛选字符串
        List<String> collect = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println("筛选后的列表：" + collect);

        // 合并字符串
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(","));
        System.out.println(mergedString);

        // 列表




    }
}