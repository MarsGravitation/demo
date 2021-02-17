package com.microwu.concurrent.executor;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description: JDK 定时任务
 *
 * https://crossoverjie.top/2019/10/14/algorithm/timer-detail/
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/21   11:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TimeDemo {

    /**
     * Timer 类
     * 大致原理：
     *  1. 内部维护了一个数组实现的最小堆，按照任务的执行时间排序，保证取数据是 O(1), 添加数据是 O(log(n))
     *  2. 添加的时候会把一些基础属性补充完毕
     *  3. 实例化的时候会启动一个线程，死循环获取堆顶元素，然后执行（或者重新调度）
     *
     * 缺陷：
     *  1. 后台调度任务的线程只有一个，导致任务是阻塞运行的，一旦其中一个任务执行周期过程，会影响到后面的任务
     *  2. 本身没有捕获其他异常，一旦出现异常导致后续任务不会执行
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
     * 原理和 Timer 大致相似：
     *  1. 自定义一个延迟队列 DelayedWorkQueue（最小堆，线程安全）
     *  2. 新建任务，并启动线程
     *  3. 死循环消费任务
     *
     * 相比 Timer 优点：
     *  1. 多线程互不影响
     *  2. 依赖线程池，单个任务出现异常不影响其他线程
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