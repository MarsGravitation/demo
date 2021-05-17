package com.microwu.design.application;

/**
 * if-else 优化思路
 *
 * if (money >= 1000) {
 *     if (type == UserType.SILVER_VIP.getCode()) {
 *
 *         System.out.println("白银会员 优惠50元");
 *         result = money - 50;
 *     } else if (type == UserType.GOLD_VIP.getCode()) {
 *
 *         System.out.println("黄金会员 8折");
 *         result = money * 0.8;
 *     } else if (type == UserType.PLATINUM_VIP.getCode()) {
 *
 *         System.out.println("白金会员 优惠50元，再打7折");
 *         result = (money - 50) * 0.7;
 *     } else {
 *         System.out.println("普通会员 不打折");
 *         result = money;
 *     }
 * }
 *
 * 
 *
 * https://juejin.cn/post/6844904018146230286
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/4/27  9:28
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class IfElseCase {
    
    /**
     * 一个电商系统，当用户消费满 1000 金额，可以根据用户VIP等级，享受打折优惠。
     *
     * 根据用户VIP等级，计算出用户最终的费用。
     *
     * 普通会员 不打折
     * 白银会员 优惠50元
     * 黄金会员 8折
     * 白金会员 优惠50元，再打7折
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/4/27     9:33
     *
     * @param 
     * @return void
     */
    public void test(long money, int type) {
        long m = 0;
        if (money > 1000) {
            if (type == 1) {
                m = money;
                return;
            } else if (type == 2) {
                m = money - 50;
                return;
            } else if (type == 3) {
                m = (long) (money * 0.8);
                return;
            } else if (type == 4) {
                m = (long) ((money - 50) * 0.7);
                return;
            } else {
                m = 0;
            }
        }
        System.out.println(m);
    }

    /**
     * 策略模式优化
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/4/27     9:38
     *
     * @param
     * @return void
     */
    public void test02() {

    }
    
}
