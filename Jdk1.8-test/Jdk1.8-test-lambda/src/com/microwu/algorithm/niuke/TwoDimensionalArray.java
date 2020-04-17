package com.microwu.algorithm.niuke;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/17   12:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TwoDimensionalArray {

    public static void test() {
        int[][] nums = {
                {1,   4,  7, 11, 15},
                {2,   5,  8, 12, 19},
                {3,   6,  9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        };
        int num = 18;
        int i = 0;
        int j = nums[0].length - 1;
        while(num != nums[i][j]) {
            if(nums[i][j] > num) {
                j--;
            }else {
                i++;
            }
            if(i == nums.length || j < 0) {
                System.out.println("没找着");
                return;
            }
        }
        System.out.println(nums[i][j]);
    }

    public static void main(String[] args) {
        test();
    }
}