package com.microwu.algorithm.sort;

import java.util.Arrays;

/**
 * Description: https://www.cnblogs.com/luomeng/p/10618709.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/26   17:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HeapSort {

    public static void test() {
        System.out.println("=============别人写的===============");

        int[] nums = {3, 5, 8, 1, 2, 9, 4, 7, 6};

        // 1. 构建大顶堆
        for (int i = nums.length / 2 - 1; i >= 0; i--) {
            // 从第一个非叶子节点, 从下至上, 从右至左, 调整
            adjustHeap(nums, i, nums.length);
        }

        System.out.println(Arrays.toString(nums));

        // 2. 调整堆结构, 并交换堆顶元素 与 末尾元素
        for (int j = nums.length - 1; j > 0; j--) {
            SortUtils.swap(nums, 0, j);
            adjustHeap(nums, 0, j);
        }

        System.out.println(Arrays.toString(nums));
    }

    /**
     * 调整堆
     *
     * @param arr
     * @param parent
     * @param length
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/27  10:37
     */
    private static void adjustHeap(int[] arr, int parent, int length) {
        // 将temp 作为父节点
        int temp = arr[parent];
        // 左孩子
        int lChild = 2 * parent + 1;

        while (lChild < length) {
            // 右孩子
            int rChild = lChild + 1;
            // 如果有右孩子节点, 并且右孩子节点的值大于左孩子节点, 则选取右孩子节点
            if (rChild < length && arr[rChild] > arr[lChild]) {
                lChild++;
            }

            // 如果父节点的值已经大于孩子节点的值, 直接结束
            if (temp > arr[lChild]) {
                break;
            }

            // 把孩子节点的值赋给父节点
            arr[parent] = arr[lChild];

            // 选取孩子节点的左孩子节点, 继续向下筛选
            parent = lChild;
            lChild = 2 * parent + 1;
        }
        // 将父节点和左孩子交换
        arr[parent] = temp;
    }

    private static void test02(int[] arr, int parent, int length) {
        int temp = arr[parent];

        int left = 2 * parent + 1;

        while (left < length) {
            int right = left + 1;
            if(right < length && arr[right] > arr[left]) {
                left = right;
            }

            if(temp > arr[left]) {
                break;
            }

            arr[parent] = arr[left];

            parent = left;
            left = 2 * parent + 1;
        }

        arr[parent] = temp;

    }

    private static void test03() {
        System.out.println("============自己写的==========");

        int[] nums = {3, 5, 8, 1, 2, 9, 4, 7, 6};

        for(int i = nums.length / 2 - 1; i >= 0; i--) {
            test02(nums, i, nums.length - 1);
        }

        System.out.println(Arrays.toString(nums));

        for(int j = nums.length - 1; j > 0; j--) {
            SortUtils.swap(nums, 0, j);
            test02(nums, 0, j);
        }

        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
        test();
        test03();
    }
}