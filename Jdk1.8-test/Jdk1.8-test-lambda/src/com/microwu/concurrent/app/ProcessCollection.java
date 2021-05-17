package com.microwu.concurrent.app;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程处理集合
 *  |- 可以按照数据分
 *  |- 也可以按照线程分
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/6  9:31
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class ProcessCollection {

    /**
     * https://blog.csdn.net/timmyroy/article/details/84920854
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/6     9:39
     *
     * @param args
     * @return void
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 开始时间
        long start = Instant.now().getEpochSecond();
        int simulationDataNum = 3000;
        ArrayList<String> list = new ArrayList<>(simulationDataNum);
        // 模拟数据
        for (int i = 0; i < simulationDataNum; i++) {
            list.add(String.valueOf(i));
        }
        // 每 500 条数据开启一个线程
        int threadSize = 500;
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记，过滤 threadNum 为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        ArrayList<Callable<Integer>> tasks = new ArrayList<>();
        Callable<Integer> task = null;
        List<String> cutList = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadNum * i, threadNum * (i + 1));
            }
            final List<String> listStr = cutList;
            task = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + "线程：" + listStr);
                    return 1;
                }
            };

            //
            tasks.add(task);

        }
        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        for (Future<Integer> future : futures) {
            System.out.println(future.get());
        }

        // 关闭线程池
        executorService.shutdown();
        System.err.println("执行任务消耗了 ：" + (Instant.now().getEpochSecond() - start) + "毫秒");

    }

    public void test02() {
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            integers.add(i);
        }

        int threadSize = 5;
        int start = 0;
        int step = integers.size() / threadSize;
        int end = step;
        for (int i = 0; i < threadSize; i++) {
            if (i == 4) {
                end = integers.size();
            }
            System.out.println(integers.subList(start, end));

            start += step;
            end += step;

        }
    }

    public void test04() {
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            integers.add(i);
        }

        int threadSize = 5;
        int start = 0;
        int remain = integers.size();
        int step = remain / threadSize;
        int end = step;
        int r = threadSize;

        for (int i = 0; i < threadSize; i++) {
            if (i == threadSize - 1) {
                end = integers.size();
            }
            System.out.println(integers.subList(start, end));

            r--;
            if (r == 0) {
                break;
            }
            remain -= end - start;
            step = remain / r;

            start = end;
            end += step;

        }
    }

}
