package com.microwu.algorithm.sort;

import java.util.Arrays;

/**
 * Description: 选择排序
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/19   10:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SelectSort {

    public static void main(String[] args) {
        int[] ints = {9, 8, 7, 6, 5};
        for(int i = 0; i < ints.length - 1; i++) {
            int k = i;
            for(int j = i + 1; j < ints.length; j++) {
                if(ints[k] > ints[j]) {
                    k = j;
                }
                if(i != k) {
                    SortUtils.swap(ints, i, k);
                }
            }
        }

        System.out.println(Arrays.toString(ints));
    }
}