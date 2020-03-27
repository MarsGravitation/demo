package com.microwu.time;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description: JDK 定时任务
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/21   11:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TimerDemo {

    /**
     * Timer 类
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  11:00
     *
     * @param
     * @return  void
     */
    private static void test() {
        new Timer("cxd").schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(LocalDateTime.now() + ": hello world ~~~");
            }
        }, 1000, 2000);

    }

    /**
     * JUC 定时任务线程池
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  11:06
     *
     * @param
     * @return  void
     */
    private static void test02() {
        System.out.println(LocalDateTime.now());
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.schedule(() -> {
            System.out.println(LocalDateTime.now() + ": hello world ~~~");
        }, 2, TimeUnit.SECONDS);

    }

    public static void main(String[] args) {
        test02();
    }
}