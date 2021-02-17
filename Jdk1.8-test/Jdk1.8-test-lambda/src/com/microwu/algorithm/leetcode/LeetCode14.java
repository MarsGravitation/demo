package com.microwu.algorithm.leetcode;

import java.util.Arrays;

/**
 * Description: 最长公共前缀
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/5   15:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode14 {

    /**
     * 自己思考
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  15:17
     *
     * @param   	strs
     * @return  java.lang.String
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }

        if (strs.length == 1) {
            return strs[0];
        }
        // 以第一个字符串作为基准，进行比较
        String result = strs[0];

        for (int i = 1; i < strs.length; i++) {
            // 进行对比
            int index = 0;
            while (index < result.length() && index < strs[i].length()) {
                if (result.charAt(index) != strs[i].charAt(index)) {
                    break;
                }
                index++;
            }
            result = result.substring(0, index);
        }
        return result;
    }

    /**
     * 官方解答 1
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  16:13
     *
     * @param   	strs
     * @return  java.lang.String
     */
    public static String longestCommonPrefix2(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        String prefix = strs[0];
        int count = strs.length;
        for (int i = 1; i < count; i++) {
            prefix = longestCommonPrefix(prefix, strs[i]);
            if (prefix.length() == 0) {
                break;
            }
        }
        return prefix;
    }

    public static String longestCommonPrefix(String str1, String str2) {
        int length = Math.min(str1.length(), str2.length());
        int index = 0;
        while (index < length && str1.charAt(index) == str2.charAt(index)) {
            index++;
        }
        return str1.substring(0, index);
    }

    /**
     * 纵向分析 - 有点问题
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  16:18
     *
     * @param   	strs
     * @return  java.lang.String
     */
    public static String longestCommonPrefix3(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        int[] lengths = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            lengths[i] = strs[i].length();
        }
        Arrays.sort(lengths);
        int length = lengths[0];
        int index = 0;
        for (int i = 0; i < length; i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (c != strs[j].charAt(i)) {
                    break;
                }
                index++;
            }
        }
        if (length == 0) {
            return "";
        }
        return strs[0].substring(0, index / length == 0 ? 0 : index / length + 1);
    }

    /**
     * 官方 2
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/6  10:54
     *
     * @param   	strs
     * @return  java.lang.String
     */
    public static String longestCommonPrefix4(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        int length = strs[0].length();
        int count = strs.length;
        for (int i = 0; i < length; i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < count; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }

    public static void main(String[] args) {
        String[] strings = {""};
        System.out.println(longestCommonPrefix3(strings));
    }

}