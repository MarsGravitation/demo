package com.microwu.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: 线程池
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/3/1   10:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ExecutorDemo {

    private static void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        final int a = 0;
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(a);
            }
        });
//        ExecutorService
//        ScheduledExecutorService
//        ThreadPoolExecutor
//        ScheduledThreadPoolExecutor
    }
}