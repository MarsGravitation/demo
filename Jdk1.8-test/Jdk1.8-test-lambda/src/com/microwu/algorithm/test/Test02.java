package com.microwu.algorithm.test;

/**
 * Description: 给定两个字节数组，判断是否存在一个在另外一个中
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/24   15:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Test02 {

    public static void main(String[] args) {
        byte[] bytes1 = {1, 0, 1, 0, 1, 0, 1, 1};
        byte[] bytes2 = {1, 0, 1, 1};

        int i = indexOf(bytes1, bytes2, 5);
        System.out.println(i);
    }

    @SuppressWarnings("Duplicates")
    private static int indexOf(byte[] b1, byte[] b2, int start) {

        int index = 0;
        for (int i = start; i < b1.length - b2.length + 1; i++) {
            for (int j = 0; j < b2.length; j++) {
                if (b2[j] != b1[i + j]) {
                    index = 0;
                    break;
                }
                index++;
            }
            if (index == b2.length) {
                return index;
            }
        }
        return -1;
    }

}