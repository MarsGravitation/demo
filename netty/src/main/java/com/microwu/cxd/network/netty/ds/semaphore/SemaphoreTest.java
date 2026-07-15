package com.microwu.cxd.network.netty.ds.semaphore;

import java.time.Instant;
import java.util.concurrent.Semaphore;

/**
 * Description:
 *
 * 1. Java 提供了 Lock，为啥还要提供一个 Semaphore
 * 常见的需求就是工作中遇到的各种池化资源。
 * 比如数据库连接池同一时刻一定是允许多个线程同时使用连接池的
 *
 * 2. 信号量机制在 Netty 优雅停机里的使用
 *  |- 写一个 demo 演示
 *  目前换成了 countDown
 *
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484459&idx=1&sn=3a6dd71201a2f7fda4ca5fe8193e9c26&chksm=fbc0922bccb71b3d80bbf5ae7b836f35fd1e74b2c4807346659122b926183c8f3ccbc844fa66&cur_album_id=1338702322071076864&scene=189#wechat_redirect
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   16:08
 * Update History:
 * Author        Time            Content
 */
public class SemaphoreTest {

    private static final Semaphore THREAD_LOCK = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        long old = Instant.now().toEpochMilli();
        System.out.println("old: " + old);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // V(++) 操作
            THREAD_LOCK.release();
        }).start();
//        THREAD_LOCK.tryAcquire();
        // permits = 0，这里阻塞
        // P(--) 操作
        THREAD_LOCK.acquire();
        long now = Instant.now().toEpochMilli();
        System.out.println("now: " + now);
        System.out.println(now - old);
    }

}
