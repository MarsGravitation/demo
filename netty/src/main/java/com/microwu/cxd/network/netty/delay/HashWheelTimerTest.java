package com.microwu.cxd.network.netty.delay;

import io.netty.util.HashedWheelTimer;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/23   16:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HashWheelTimerTest {

    /**
     * 时间轮实现延时任务
     *
     * 优点：效率高，在大量高负荷的任务堆积情况下，HashWheelTimer 基本上要比 delayQueue 低一倍的延迟率
     * 缺点：内存占用相对较高，对时间精确度要求相对不高，持久化
     *
     * 如果系统重启的话，保存在内存的数据将会丢失，这里可以将数据保存在数据库中，重启时从数据库中获取数据
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/23  16:07
     *
     * @param
     * @return  void
     */
    public static void test() {
        final HashedWheelTimer timer = new HashedWheelTimer();
        timer.newTimeout(timeout -> {
            System.out.println("timeout 5");
        }, 5, TimeUnit.SECONDS);
    }

    public static void test02() {

    }

    public static void main(String[] args) {
        test();
    }

}