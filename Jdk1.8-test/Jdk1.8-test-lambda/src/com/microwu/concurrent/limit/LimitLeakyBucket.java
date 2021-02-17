package com.microwu.concurrent.limit;

/**
 * Description: 漏桶算法
 *  水（请求）先进入漏桶里，漏桶以一定的速度出水（接口有响应速率），当水流入速度过大会直接溢出（访问频率超过接口响应速率），
 *  然后就拒绝请求
 *
 *  两个变量：桶的大小支持流量突发增多时可以存多少水（burst）
 *          水桶漏洞的大小（rate）
 *
 * https://tianyalei.blog.csdn.net/article/details/74942405
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/29   17:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LimitLeakyBucket {

//    /**
//     * 令牌的发放速率
//     * 每一秒方法多少
//     */
//    private final long rate;
//
//    /**
//     * 容量
//     */
//    private final long capacity;
//
//    /**
//     * 水量
//     */
//    private volatile long water;
//
//    /**
//     * 上一次的更新时间
//     */
//    private volatile long lastUpdateTime;
//
//    public LimitLeakyBucket(long rate) {
//        this.rate = rate;
//        this.capacity = this.rate * 8;
//        this.water = rate;
//        this.lastUpdateTime = System.currentTimeMillis();
//    }
//
//    /**
//     *  1. 未加满水：water += 1
//     *  2. 漏水：通过时间差来计算漏水量
//     *  3. 剩余水量：总水量 - 漏水量
//     *
//     * @author   chengxudong               chengxudong@microwu.com
//     * @date    2020/12/29  17:43
//     *
//     * @param
//     * @return  boolean
//     */
//    public synchronized boolean acquire() {
//        long now = System.currentTimeMillis();
//        // 先执行漏水，计算剩余水量
//        long durationMs = now - lastUpdateTime;
//        long leakyWater = (long) (durationMs * 1.0 * rate / 1000);
//        water = Math.max(0, water - leakyWater);
//
//        // 如果漏水量较小，直接返回。避免开始时，大流量通过
//        if (leakyWater < 1) {
//            return false;
//        }
//
//        if ((water + 1) < capacity) {
//            // 尝试加水，并且水还没满
//            water++;
//
//            lastUpdateTime = now;
//            return true;
//        } else {
//            // 水满，拒绝加水
//            return false;
//        }
//    }

    private long rate = 3;
    private long burst = 3;

    private long refreshTime;
    private long water;

    private void refreshWater() {
        long now = System.currentTimeMillis();

        // 水随着时间流逝，不断流走，最小 0
        water = Math.max(0, water - (now - refreshTime) * rate / 1000);
        refreshTime = now;
    }

    public boolean permissionGranted() {
        refreshWater();
        if (water < burst) {
            water++;
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LimitLeakyBucket bucket = new LimitLeakyBucket();
        for (int i = 0; i < 10; i++) {
            System.out.println(bucket.permissionGranted());
            Thread.sleep(100);
        }
    }

}