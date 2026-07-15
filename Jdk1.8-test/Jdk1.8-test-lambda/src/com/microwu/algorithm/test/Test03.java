package com.microwu.algorithm.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/4/24   10:43
 * Update History:
 * Author        Time            Content
 */
public class Test03 {

    /**
     * 12345  转成中文金额
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/4/24 10:43
     *
     * @param: args - [java.lang.String]
     * @return: void
     */
    public static void main(String[] args) {
        // 映射
        Map<String, String> map = new HashMap<>();
        map.put("1", "壹");

        String[] arr = {"元", "拾"};
        String[] arr2 = {"角", "分"};

        double num = 11.11;
        // 1. 数字转字符串
        String s = Double.toString(num);

        int separator = s.indexOf(".");
        // 整数部分
        String integer = s.substring(0, separator);
        // 小数部分
        String decimal = s.substring(separator + 1);

        // 2. 遍历
        String result = "";
        int index = 0;
        for (int i = integer.length() - 1; i >= 0; i--) {
            result = map.get(String.valueOf(s.charAt(i))) + arr[index++] + result;
        }

        index = 0;
        // 小数
        for (int i = 0; i < decimal.length(); i++) {
            result += (map.get(String.valueOf(s.charAt(i))) + arr2[index++]);
        }

        System.out.println(result);
    }
    
}
