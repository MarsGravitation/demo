package com.microwu.cxd.multithreading.status;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/6   10:28
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class InterruptDemo {
    public static void main(String[] args) {
        int count = 0;
        Thread sleepThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread busyThread = new Thread(() -> {
            while (true) {

            }
        });
        sleepThread.start();
        busyThread.start();

        sleepThread.interrupt();
        busyThread.interrupt();
        while(sleepThread.isInterrupted()){
            ++count;
        }
        System.out.println("sleepThread: " + sleepThread.isInterrupted());
        System.out.println("busyThread: " + busyThread.isInterrupted());
        System.out.println(count);

    }
}