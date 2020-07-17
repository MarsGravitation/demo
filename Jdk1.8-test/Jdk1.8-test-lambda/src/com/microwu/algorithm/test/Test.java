package com.microwu.algorithm.test;

import java.util.ArrayList;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/8   13:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Test {

    public String[] minSubstring(String string) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < string.length() - 1; i++) {
            char c = string.charAt(i);
            if (c < '0' || c > '9') {
                // 不是数字，继续
                continue;
            }
            // 是数字，和后一个进行比较
            char c1 = string.charAt(i + 1);
            if (c1 > c) {
                // 找到比后面大的字符，创建一个新的字符串
                StringBuilder stringBuilder = new StringBuilder(c);
            } else {
                // 不比后面大

            }
        }
        return null;
    }

    public static void main(String[] args) {

    }
}