package com.microwu.concurrent.limit.houbb;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 滑动窗口
 * 该窗口同样的 key，都是单线程计算
 *
 * https://blog.csdn.net/tianyaleixiaowu/article/details/102861254
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/29   11:04
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SlidingWindow {

    /**
     * 循环队列，就是装多个窗口用，该数量是 windowSize 的 2 倍
     */
    private AtomicInteger[] timeSlices;

    /**
     * 队列的总长度
     */
    private int timeSliceSize;

    /**
     * 每个时间片的时长，以毫秒为单位
     */
    private int timeMillisPerSlice;

    /**
     * 共有多少个时间片（即窗口长度）
     */
    private int windowSize;

    /**
     * 在一个完整窗口期内允许通过的最大阈值
     */
    private int threshold;

    /**
     * 该滑动窗口的起始创建时间，也就是第一个数据
     */
    private long beginTimestamp;

    /**
     * 最后一个数据的时间戳
     */
    private long lastAddTimestamp;

    public SlidingWindow(int duration, int threshold) {
        if (duration > 600) {
            duration = 600;
        }
        if (duration <= 5) {
            this.windowSize = 5;
            this.timeMillisPerSlice = duration * 200;
        } else {
            this.windowSize = 10;
            this.timeMillisPerSlice = duration * 100;
        }
        this.threshold = threshold;
        // 保证存储至少两个 window
        this.timeSliceSize = windowSize * 2;
        
        reset();
    }

    public SlidingWindow(int timeMillisPerSlice, int windowSize, int threshold) {
        this.timeMillisPerSlice = timeMillisPerSlice;
        this.windowSize = windowSize;
        this.threshold = threshold;
        // 保证存储在至少两个window
        this.timeSliceSize = windowSize * 2;

        reset();
    }

        /**
         * 初始化
         *
         * @author   chengxudong               chengxudong@microwu.com
         * @date    2020/12/29  11:13
         *
         * @param
         * @return  void
         */
    private void reset() {
        beginTimestamp = SystemClock.now();
        // 窗口个数
        AtomicInteger[] localTimeSlices = new AtomicInteger[timeSliceSize];
        for (int i = 0; i < timeSliceSize; i++) {
            localTimeSlices[i] = new AtomicInteger(0);
        }
        timeSlices = localTimeSlices;

    }

    private void print() {
        for (AtomicInteger integer : timeSlices) {
            System.out.print(integer + " - ");
        }
        System.out.println();
    }

    private int locationIndex() {
        long now = SystemClock.now();
        // 如果当前的 key 已经超过一个时间片了，那么就直接初始化就行了，不用去计算了
        if (now - lastAddTimestamp > timeMillisPerSlice * windowSize) {
            reset();
        }

        return (int) (((now - beginTimestamp) / timeMillisPerSlice) % timeSliceSize);
    }

    public synchronized boolean addCount(int count) {
        // 当前自己所在的位置，是那个小时间窗
        int index = locationIndex();
        // 清空自己前面 windowSize 到 2 * windowSize 之间的数据格的数据
        clearFromIndex(index);

        int sum = 0;
        // 在当前时间片里继续 + 1
        sum += timeSlices[index].addAndGet(count);

        // 加上前面几个时间片
        for (int i = 1; i < windowSize; i++) {
            sum += timeSlices[(index - i + timeSliceSize) % timeSliceSize].get();
        }
        System.out.println(sum + " ---- " + threshold);

        lastAddTimestamp = SystemClock.now();

        return sum >= threshold;
    }

    private void clearFromIndex(int index) {
        for (int i = 1; i <= windowSize; i++) {
            int j = index + i;
            if (j >= windowSize * 2) {
                j -= windowSize * 2;
            }
            timeSlices[j].set(0);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SlidingWindow window = new SlidingWindow(250, 4, 10);
        for (int i = 0; i < 10; i++) {
            System.out.println(window.addCount(2));

            window.print();
            System.out.println("---------------------");
            Thread.sleep(100);
        }
    }

}