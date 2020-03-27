package com.microwu.concurrent.limit;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/11   14:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AtomicRateLimit implements Runnable {

    private static AtomicInteger count = new AtomicInteger(0);

    private Integer limitTime;

    public AtomicRateLimit(Integer limitTime) {
        this.limitTime = limitTime;
    }


    /**
     * time: 时间
     * count: 次数
     * 在10秒内, 最多10次请求
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/11  14:51
     *
     * @param   	limitCount
     * @return  boolean
     */
    public boolean limit(Integer limitCount) {
        if(count.get() >= limitCount) {
            System.out.println("Thread :" + Thread.currentThread() + " 被拦住...");
            return false;
        }
        count.incrementAndGet();
        System.out.println("Thread :" + Thread.currentThread() + " 通过... DateTime: " + LocalDateTime.now());
        return true;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(limitTime);
            count.getAndSet(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        AtomicRateLimit atomicRateLimit = new AtomicRateLimit(10);
        new Thread(atomicRateLimit).start();
        for(int i = 0; i < 30; i++) {

            if(i == 15) {
                TimeUnit.SECONDS.sleep(11);
            }
            executorService.execute(() -> {
                atomicRateLimit.limit(10);
            });
        }

    }
}