package com.microwu.concurrent.limit;

/**
 * Description:
 *
 *  https://www.jianshu.com/p/36bca4ed6d17
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/16   16:25
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LimitTokenBucket {

    /**
     * 令牌的发放速率
     *  每一秒发放多少
     */
    private final long rate;

    /**
     * 容量
     */
    private final long capacity;

    /**
     * 令牌数量
     */
    private volatile long tokenNum;

    /**
     * 上一次更新的时间
     */
    private volatile long lastUpdateTime;

    public LimitTokenBucket() {
        this.rate = 2L;

        this.capacity = this.rate;

        this.tokenNum = 0;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public synchronized boolean acquire() {
        if (tokenNum < 1) {
            long now = System.currentTimeMillis();
            long duration = now - lastUpdateTime;
            long newTokenNum = (long) (duration * 1.0 * rate / 1000);
//            System.out.println(newTokenNum);
            if (newTokenNum > 0) {
                this.tokenNum = Math.min(newTokenNum + this.tokenNum, capacity);
                lastUpdateTime = now;
            } else {
                return false;
            }
        }

        this.tokenNum--;
        return true;
    }

    public static void main(String[] args) {
        final LimitTokenBucket bucket = new LimitTokenBucket();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 6; j++) {
                    while (!bucket.acquire()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    System.out.println(System.currentTimeMillis() / 1000 + ": " + Thread.currentThread().getName());
                }
            }).start();
        }
    }

}