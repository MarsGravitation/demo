package com.microwu.algorithm.test;

/**
 * 杨辉三角
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/10/22  16:17
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class YhTriangleTest {

    public static void triangle(int n) {
        StringBuilder sb = new StringBuilder();
        int[][] yh = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    yh[i][j] = 1;
                } else {
                    yh[i][j] = yh[i - 1][j - 1] + yh[i - 1][j];
                }
                sb.append(yh[i][j]);
            }
            sb.append("\r\n");
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        triangle(5);
    }

}
