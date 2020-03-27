package com.microwu.concurrent.sync;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/2   17:13
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class InterruptedThread {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    if(this.isInterrupted()) {
                        System.out.println("线程中断...");
                        break;
                    }
                }
                System.out.println("跳出循环");
            }
        };
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread.interrupt();
    }
}