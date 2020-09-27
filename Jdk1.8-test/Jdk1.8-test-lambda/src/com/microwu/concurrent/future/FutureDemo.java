package com.microwu.concurrent.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Description: Java 技术驿站
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/24   9:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FutureDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(1000);
            return 100 * 100;
        });
        // cancel 停止操作并中断底层线程
//        future.cancel(true);

        // isDone 用于获取任务是否已完成
        while(!future.isDone()) {
            System.out.println("Calculating...");
            Thread.sleep(300);
        }

        // get 返回实际结果，此方法是一个阻塞方法
        // 如果任务还没执行完毕，就会一直阻塞
        Integer result = future.get();
        System.out.println(result);

    }
}