package com.microwu.cxd.common.construct;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   16:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RingBufferWheelTest {

    public static void test() throws InterruptedException {
        RingBufferWheel wheel = new RingBufferWheel(Executors.newFixedThreadPool(2));
        while (true) {
            TimeUnit.SECONDS.sleep(1);

            ByteTask task = new ByteTask(0);
            task.setKey(1);
            wheel.addTask(task);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        test();

    }

    private static class ByteTask extends RingBufferWheel.Task {
        private byte[] b;

        public ByteTask(int size) {
            this.b = new byte[size];
        }
    }

}