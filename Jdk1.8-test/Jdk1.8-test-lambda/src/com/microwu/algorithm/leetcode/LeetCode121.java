package com.microwu.algorithm.leetcode;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/6   14:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LeetCode121 {

    /**
     * 暴力破解
     *  思路：我们需要找出两个数字之间的最大差值
     *  时间复杂度：n^2
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/6  14:53
     *
     * @param   	prices
     * @return  int
     */
    public static int maxProfit(int[] prices) {
        int maxProfit = 0;
        for (int i = 0; i < prices.length; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                if (prices[i] < prices[j]) {
                    maxProfit = Math.max(maxProfit, prices[j] - prices[i]);
                }
            }
        }
        return maxProfit;
    }

    /**
     * 假设在最低点买股票，用 minPrice 记录历史最低价格，然后每天都认为是在历史最低点买入
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2021/1/6  15:06
     *
     * @param   	prices
     * @return  int
     */
    public static int maxProfit2(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int i = 0; i <prices.length; i++) {
//            minPrice = minPrice > prices[i] ? prices[i] : minPrice;
//            int profit = prices[i] - minPrice;
//            maxProfit = profit > maxProfit ? profit : maxProfit;
            if (prices[i] < minPrice) {
                minPrice = prices[i];
            } else if (prices[i] - minPrice > maxProfit) {
                maxProfit = prices[i] - minPrice;
            }
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        int[] prices = new int[]{7, 1, 5, 3, 6, 4};
        System.out.println(maxProfit2(prices));
    }
}