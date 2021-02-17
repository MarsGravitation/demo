package com.microwu.algorithm.blog.backtrack;

/**
 * Description: 斐波那契数列
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/4   11:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FibTest {

    /**
     * 暴力递归
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  11:41
     *
     * @param   	n
     * @return  int
     */
    public int fib(int n ) {
        if (n == 1 || n == 2) {
            return 1;
        }
        return fib(n -1) + fib(n - 2);
    }

    /**
     * 数组充当备忘录
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  11:43
     *
     * @param   	n
     * @return  int
     */
    public int fib2(int n) {
        if (n < 1) {
            return 0;
        }
        int[] nums = new int[n + 1];
        return helper(nums, n);
    }

    private int helper(int[] nums, int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        if (nums[n] != 0) {
            return nums[n];
        }
        nums[n] = helper(nums, n - 1) + helper(nums, n - 2);
        return nums[n];
    }

    /**
     * dp 数组的迭代解法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  13:39
     *
     * @param   	n
     * @return  int
     */
    public int fib3(int n) {
        int[] dp = new int[n];
        dp[1] = dp[2] = 1;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * 只存储两个状态，不需要使用 dp 存储所有状态
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  13:41
     *
     * @param   	n
     * @return  int
     */
    public int fib4(int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        int prev = 1, curr = 1;
        for (int i = 3; i <=n; i++) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }
        return curr;
    }

}