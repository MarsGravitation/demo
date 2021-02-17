package com.microwu.algorithm.blog.backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 凑零钱
 *  给你 k 种面值的硬币，面值分别为 c1, c2 ... ck, 每种硬币的数量无限，再给一个总金额 amount，问你最少需要几枚硬币凑出这个金额
 *
 *  递归的解题思路: 从上而下
 *      1. 先确定一个函数，明确这个函数的功能
 *      2. 推导问题与子问题具有的相同解决思路
 *      3. 找到最终不可再分解的子问题的解，即临界条件
 *      4. 将公式补充到定义的函数中
 *      5. 改造
 *
 *  考虑：
 *      1. 凑 amount
 *      2. 如果要凑 amount，要得到 amount - n，n 是 coins 中的一个，找到 amount - n 最小的那一个
 *      3. 临界条件，n = 0 或者 n < 0
 *
 *      a. dp(amount)
 *      b. dp(amount) = for (int coin : coins) {
 *          res = min(res, 1 + dp(n - coin))
 *      }
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/4   13:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CollectChangeTest {

    /**
     * 算硬币
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  13:45
     *
     * @param   	coins 硬币面值
     * @param 		amount 目标金额
     * @return  int
     */
    public int coinChange(int[] coins, int amount) {
        return dp(coins, amount);
    }

    /**
     * 暴力递归解法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  13:58
     *
     * @param   	n
     * @param 		coins
     * @return  int
     */
    private int dp(int[] coins, int n) {
        if (n == 0) {
            return 0;
        }
        if (n < 0) {
            return -1;
        }

        int res = Integer.MAX_VALUE;
        for (int coin : coins) {
            int subProblem = dp(coins, n - coin);
            if (subProblem == -1) {
                continue;
            }
            res = Math.min(res, 1 + subProblem);
        }
        return res != Integer.MAX_VALUE ? res : -1;
    }

    /**
     * 备忘录的递归
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/4  14:46
     *
     * @param   	coins
     * @param 		amount
     * @return  int
     */
    public int coinChange2(int[] coins, int amount) {
        List<Integer> memo = new ArrayList<>();
        return dp(coins, amount, memo);
    }

    private int dp(int[] coins, int amount, List<Integer> memo) {
        if (memo.contains(amount)) {
            return memo.get(amount);
        }

        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }
        int res = Integer.MAX_VALUE;
        for (int coin : coins) {
            int subProblem = dp(coins, amount, memo);
            if (subProblem == -1) {
                continue;
            }
            res = Math.min(res, 1 + subProblem);
        }

        memo.set(amount, res != Integer.MAX_VALUE ? res : -1);
        return memo.get(amount);
    }

    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;
        CollectChangeTest cct = new CollectChangeTest();
        System.out.println(cct.coinChange(coins, amount));
    }

}