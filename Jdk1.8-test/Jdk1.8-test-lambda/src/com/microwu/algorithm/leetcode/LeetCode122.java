package com.microwu.algorithm.leetcode;

/**
 * Description: 买卖股票的最佳时机
 *  回溯算法就是一个决策树的遍历过程，需要思考三个问题
 *      a. 路径：也就是已经做出的选择
 *      b. 选择列表，也就是你当前可以做的选择
 *      c. 结束条件：也就是到达决策树底层
 *
 *  框架：
 *      result = []
 *      def backtrack(路径， 选择列表) ：
 *          if 满足结束条件：
 *              result.add(路径)
 *              return;
 *          for 选择 in 选择列表
 *              做选择
 *              backtrack(路径，选择列表)
 *              撤销选择
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/6   11:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode122 {

    private static int res;

    /**
     * 暴力搜索 - 回溯法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/6  11:12
     *
     * @param   	prices
     * @return  int
     */
    public static int maxProfit(int[] prices) {
        int len = prices.length;
        if (len < 2) {
            return 0;
        }
        dfs(prices, 0, len, 0, res);
        return res;
    }

    /**
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/6  11:17
     *
     * @param   	prices 股票数组
     * @param 		index 当前是第几天，从 0 开始
     * @param 		len
     * @param 		status 0 表示不持有股票，1 表示持有股票
     * @param 		profit 当前收益
     * @return  void
     */
    private static void dfs(int[] prices, int index, int len, int status, int profit) {
        if (index == len) {
            res = Math.max(res, profit);
            return;
        }

        dfs(prices, index + 1, len, status, profit);

        if (status == 0) {
           dfs(prices, index + 1, len, 1, profit - prices[index]);
        } else {
            dfs(prices, index + 1, len, 0, profit + prices[index]);
        }

    }

    private static void stock(int[] prices, int[] chooses, int status, int index, int profit) {
        if (index == prices.length) {
            res = Math.max(res, profit);
            return;
        }

        if (status == 1) {
            profit -= prices[index];
            chooses[0] = 0;
            chooses[2] = 3;
        } else if (status == 3) {
            profit += prices[index];
            chooses[0] = 1;
            chooses[2] = 0;
        }

        index++;
        // 买、不买
        for (int i = 0; i < chooses.length; i++) {
            if (chooses[i] != 0) {
                stock(prices, chooses, chooses[i], index, profit);
            }
        }
    }

    public static int maxProfit2(int[] prices) {
        int[] chooses = new int[]{1, 2, 0};
        stock(prices, chooses, 1, 0, res);
        stock(prices, chooses, 2, 0, res);
        return res;
    }

    public static void main(String[] args) {
        int[] prices = new int[]{7, 1};

        System.out.println(maxProfit2(prices));
    }
}