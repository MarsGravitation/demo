package com.microwu.algorithm.leetcode;

import java.util.Arrays;

/**
 * Description: 加一
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/7   13:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode61 {

    public static int[] plusOne(int[] digits) {
        int i = digits.length - 1;
        while (i >= 0) {
            if (digits[i] != 9) {
                digits[i] += 1;
                break;
            } else {
                digits[i--] = 0;
            }
        }
        if (i == -1) {
            int[] copy = new int[digits.length + 1];
            System.arraycopy(digits, 0, copy, 1, digits.length);
            copy[0] = 1;
            digits = copy;
        }
        return digits;
    }

    public static void main(String[] args) {
        int[] digits = new int[]{9, 9, 9};
        plusOne(digits);
        System.out.println(Arrays.toString(digits));
    }

}