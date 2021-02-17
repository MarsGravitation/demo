package com.microwu.concurrent.limit.houbb;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/29   9:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LimitFixedWindow extends LimitAdaptor {

    private final LimitContext context;

    private AtomicInteger counter = new AtomicInteger(0);

    private CountDownLatch latch = new CountDownLatch(1);

    public LimitFixedWindow(LimitContext context) {
        this.context = context;

        // 定时将 count 清零
        final long interval = context.getInterval();
        final TimeUnit timeUnit = context.getTimeUnit();

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(this::initCounter, 0, interval, timeUnit);
    }

    @Override
    public synchronized void acquire() {
        if (counter.get() >= this.context.getCount()) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int value = this.counter.incrementAndGet();
        this.latch = new CountDownLatch(1);
        System.out.println(value);
    }

    private void initCounter() {
        if (this.counter.get() >= this.context.getCount()) {
            latch.countDown();
        } else {
            this.counter = new AtomicInteger(0);
        }
    }

}