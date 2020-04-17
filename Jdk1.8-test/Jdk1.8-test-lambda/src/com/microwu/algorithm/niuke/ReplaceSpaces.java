package com.microwu.algorithm.niuke;

/**
 * Description:
 *  将一个字符串中的空格替换成 "%20"。
 *
 *  Input:
 *  "A B"
 *
 *  Output:
 *  "A%20B"
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/17   13:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReplaceSpaces {

    private static void test() {
        String str = "A B A";
        System.out.println(str.replace(" ", "%20"));
    }

    private static void test02() {
        String str = "A B A";

    }

    public static void main(String[] args) {
        test();
    }
}