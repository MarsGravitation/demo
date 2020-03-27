package com.microwu.algorithm.sort;

import java.util.Arrays;

/**
 * Description: 冒泡排序
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/19   10:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] ints = {9, 8, 7, 6, 5};
        for(int i = 1; i < ints.length; i++) {
            for(int j = ints.length - 1; j >=i; j--) {
                if(ints[j] < ints[j -1]) {
                    SortUtils.swap(ints, j, j - 1);
                }
            }
        }
        System.out.println(Arrays.toString(ints));

    }
}