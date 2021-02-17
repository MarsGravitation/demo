package com.microwu.cxd.bit;

/**
 * Description: 位运算
 *
 * 奇技淫巧：https://www.cnblogs.com/RioTian/category/1687764.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/31   13:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BitManipulationDemo {

    /**
     *  1. Java 中的进制
     *      二进制：前置 0b/0B
     *      八进制：前置 0
     *      十进制：无需前置，默认
     *      十六进制：前置 0x/0X
     *
     * 5
     * 101
     * 65
     * 1000001
     * 101
     * 1100101
     * 257
     * 100000001
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  13:20
     *
     * @param
     * @return  void
     */
    public static void test() {
        // 二进制
        int i = 0B101;
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));

        // 八进制
        i = 0101;
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));

        // 十进制
        i = 101;
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));

        // 十六进制
        i = 0X101;
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));
    }

    /**
     * JDK 进制转换
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  13:25
     *
     * @param
     * @return  void
     */
    public static void test02() {
        int i = 192;
        System.out.println("--------------------------------");
        System.out.println("10 -> 2: " + Integer.toBinaryString(i));
        System.out.println("10 -> 8: " + Integer.toOctalString(i));
        System.out.println("10 -> 16: " + Integer.toHexString(i));

        // 统一利用 Integer.valueOf, parseInt 也可以
        System.out.println("--------------------------------");
        System.out.println("2 -> 10: " + Integer.valueOf("11000000", 2));
        System.out.println("8 -> 10: " + Integer.valueOf("300", 8));
        System.out.println("16 -> 10: " + Integer.valueOf("c0", 16));
    }

    /**
     * 证明 long 是 64 位
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  13:31
     *
     * @param
     * @return  void
     */
    public static void test03() {
        long l = 100L;
        // 如果不是最大值，前面都是 0，输出的时候就不会有那么长
        System.out.println(Long.toBinaryString(l));
        System.out.println(Long.toBinaryString(l).length());

        System.out.println("=============================");
        // 2^63 - 1
        l = Long.MAX_VALUE;
        // 正数长度位 63 首位为符号位，0 代表正数
        System.out.println(Long.toBinaryString(l));
        System.out.println(Long.toBinaryString(l).length());

        System.out.println("=============================");
        l = Long.MIN_VALUE;
        // 负数长度为 64 位
        System.out.println(Long.toBinaryString(l));
        System.out.println(Long.toBinaryString(l).length());
    }

    /**
     * Java 支持的位运算符
     *  &: 按位与
     *  |: 按位或
     *  ~: 按位非
     *  ^: 按位与或
     *  <<: 左位移运算
     *  >>: 右位移运算
     *  >>>: 无符号右移运算
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  13:36
     *
     * @param
     * @return  void
     */
    public static void test04() {
        int i = 0B100;
        int j = 0B101;

        // 同为 1 则为 1，否则为 0
        System.out.println("&: " + (i & j));
        // 同为 0 则 0，否则为 1
        System.out.println("|: " + (i | j));
        // 0 为 1， 1 为 0
        System.out.println("~: " + ~i);
        // 相同为 0，不同为 1
        System.out.println("^: " + (i ^ j));
        // 全部位数左移若干位，效果等于 十进制乘以 2 的 N 次方
        System.out.println("<<: " + (i << 3));
        // 全部位数右移动若干位，把右边 N 位直接砍掉，正数高位补 0，负数高位补 1
        System.out.println(">>: " + (i >> 1));
        // 无论正数还是负数，高位通通补 0
        System.out.println(">>>: " + (i >>> 1));
    }

    public static void test05() {
        // 生成一个 8 位的随机数
        int a = 2;
        int b = 2;
        int c = 4;

        int aMask = (1 << 2) - 1;
        int bMask = (1 << 2) - 1;
        int cMask = (1 << 4) - 1;

        int a1 = 2;
        int b1 = 3;
        int c1 = 12;

        int num = a1 << 6 | b1 << 4 | c1;
        System.out.println(Integer.toBinaryString(num));

        System.out.println("a1: " + (num >> 6 & aMask));
        System.out.println("b1: " + (num >> 4 & bMask));
        System.out.println("c1: " + (num & cMask));
    }

    public static void main(String[] args) {
//        test();
//        test02();
//        test03();
        test05();
    }
}