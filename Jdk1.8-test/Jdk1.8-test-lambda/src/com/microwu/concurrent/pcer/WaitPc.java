package com.microwu.concurrent.pcer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   16:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WaitPc {

    static class Producer implements Runnable {
        private List<Integer> list;
        private int maxLength;

        public Producer(List list, int maxLength) {
            this.list = list;
            this.maxLength = maxLength;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (list) {
                    while(list.size() == maxLength) {
                        System.out.println("生产者 " + Thread.currentThread().getName() + " list 已达到最大容量, 进行wait ... ");
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Random random = new Random();
                    int i = random.nextInt();
                    System.out.println("生产者 " + Thread.currentThread().getName() + " 生产数据 " + i);
                    list.add(i);
                    list.notifyAll();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private List<Integer> list;

        public Consumer(List list) {
            this.list = list;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (list) {
                    while (list.isEmpty()) {
                        System.out.println("消费者 " + Thread.currentThread().getName() + " list 为空, 等待生产者生产...");
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("消费者 " + Thread.currentThread().getName() + " 消费数据:" + list.remove(0));
                    list.notifyAll();

                }
            }

        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> integers = new LinkedList<>();
        ExecutorService pool = Executors.newFixedThreadPool(15);
        for(int i = 0; i < 5; i++) {
            pool.submit(new Producer(integers, 8));
        }

        for(int i = 0; i < 10; i++) {
            pool.submit(new Consumer(integers));
        }

    }
}