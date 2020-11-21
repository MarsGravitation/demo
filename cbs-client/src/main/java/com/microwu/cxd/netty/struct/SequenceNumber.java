package com.microwu.cxd.netty.struct;

/**
 * Description: 生成序列，主要是用于生成 TransactionID
 *  方案一：synchronized 保证线程安全
 *  方案二：使用 CAS 保证线程安全
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/5   11:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SequenceNumber {
    /**
     * 超过最大值后的起始 number
     */
    public static final int DEFAULT_VALUE = 0x00000001;

    private static int value = DEFAULT_VALUE;

    /**
     * 获取此序列方案的下一个数字
     * 因为被 synchronized 修饰，因此此方法是线程安全的
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/5  11:48
     *
     * @param
     * @return  int
     */
    public static synchronized int next() {
        // 下一个值是当前值
        int nextValue = value;

        if (value == Integer.MAX_VALUE) {
            // 超出范围重置
            value = DEFAULT_VALUE;
        } else {
            value++;
        }
        return nextValue;
    }

    /**
     * 获取此序列方案的下一个数字
     * 当时多次调用获得是同一个，如果想要递增，请使用 next
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/5  11:50
     *
     * @param
     * @return  int
     */
    public static synchronized int peek() {
        return value;
    }
}