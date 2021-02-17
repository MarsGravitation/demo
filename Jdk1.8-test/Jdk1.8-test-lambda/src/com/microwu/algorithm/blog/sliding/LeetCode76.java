package com.microwu.algorithm.blog.sliding;

import java.util.HashMap;

/**
 * Description: 最小覆盖子串
 *  输入 S，T，请在 S 里面找出包含 T 所有字母的最小子串
 *
 *  1. 暴力破解
 *  2. 滑动窗口思路：
 *      a. 初始化 left = right = 0，把索引左闭右开区间 [left , right) 作为一个窗口
 *      b. 不断增加 right 指针扩大窗口 [left, right)，直到窗口中的字符串符合要求
 *      c. 停止增加 right，而增加 left，直到窗口中的字符串不再符合要求了
 *      4. 重复第 2 和 第 3 步，知道 right 到达字符串 S 的尽头
 *
 *      在第二步找到一个可行解，在第三步优化解
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/5   13:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode76 {

    String minWindow(String s, String t) {
        HashMap<Character, Integer> need = new HashMap<>();
        HashMap<Character, Integer> window = new HashMap<>();

        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0;
        // 记录最小覆盖子串的起始索引及长度
        int start =0, len = Integer.MAX_VALUE;
        while (right < s.length()) {
            // c 是将移入窗口的字符
            char c = s.charAt(right);
            // 右移窗口
            right++;
            // 进行窗口内数据的一系列更新
            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c))) {
                    valid++;
                }
            }

            // 判断左侧窗口是否要收缩
            while (valid == need.size()) {
                // 在这里更新最小覆盖子串
                if (right - left < left) {
                    start = left;
                    len = right - left;
                }

                // d 是将移除窗口的字符
                char d = s.charAt(left);
                // 左移窗口
                left++;
                // 进行窗口内数据的一系列更新
                if (need.containsKey(d)) {
                    if (window.get(c).equals(need.get(c))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }
        }

        return len == Integer.MAX_VALUE ? "" : s.substring(start, len);
    }

}