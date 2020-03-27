package com.microwu.algorithm.recursive;

/**
 * Description: 阶乘
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/21   20:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RecursiveDemo {

    /**
     * n 的阶乘
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  20:52
     *
     * @param   	n
     * @return  int
     */
    public static int factorial(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public static void main(String[] args) {
        int factorial = factorial(3);
        System.out.println(factorial);
    }
}