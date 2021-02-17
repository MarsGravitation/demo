package com.microwu.algorithm.blog.search;

/**
 * Description: 二分查找
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/5   10:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BinarySearchTest {

    int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        // 为什么用 <=
        // 因为 right = nums.length - 1
        // 这里找的是 [0, length -1] 的区间
        // 考虑到有没有漏的？
        // 这里终止的条件是 left > right，也就是当 left = right 还会搜索
        while (left <= right) {
            int mid = (left + right) / 2;
            // 找到了
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                // 为什么 +1
                // 这里采用的是搜索闭区间
                // [left, right]
                // 如果 mid 不是 target，下一步去哪里搜索 [mid + 1, right]
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 寻找左侧边界的二分搜索
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/5  11:34
     *
     * @param   	nums
     * @param 		target
     * @return  int
     */
    int leftBound(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }
        int left = 0;
        int right = nums.length;

        // 为什么是 <
        // 因为 right = length
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                // 关键点在于找到 target 不返回，在区间 [left, mid) 继续搜索
                right = mid;
            } else if (nums[mid] < target) {
                // 因为是半开区间，所以二分以后 [left, mid) - [mid + 1, right)
                // mid 上一步已经检测过了
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid;
            }
        }
        return left;
    }

}