package com.microwu.concurrent.limit.houbb;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description: 用于解决高并发下 System.currentTimeMillis 卡顿
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/29   11:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SystemClock {
    
    private final int period;
    
    private final AtomicLong now;
    
    private static class InstanceHolder {
        private static final SystemClock INSTANCE = new SystemClock(50);
    }
    
    private SystemClock(int period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "System Clock");
                thread.setDaemon(true);
                return thread;
            }
        });
        executor.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), 0, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return now.get();
    }

    public static long now() {
        return instance().currentTimeMillis();
    }

}