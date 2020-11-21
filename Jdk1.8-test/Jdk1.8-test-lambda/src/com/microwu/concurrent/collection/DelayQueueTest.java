package com.microwu.concurrent.collection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Description: DelayQueue
 * DelayQueue 是一个支持延时获取元素的无界阻塞队列。队列使用 DelayQueue 来实现。队列中的元素必须实现 Delayed 接口，在创建元素是可以指定多久才能从队列中获取元素。
 * 只有在延迟期满时才能从队列中提取元素。
 *
 * 1. DelayQueue 特点
 *  队列中的元素必须实现 Delayed 接口
 *
 *  // 此接口的实现必须定义一个 compareTo 方法，该方法提供与此接口的 getDelay 方法一致的排序。
 *  public interface Delayed extends Comparable<Delayed> {
 *     // 返回与此对象相关的剩余有效时间，以给定的时间单位表示
 *     long getDelay(TimeUnit unit);
 *  }
 *
 *  如果一个类实现了 Delayed 接口，当创建该类的对象并添加到 DelayedQueue 中，只有该对象的 getDelay 方法返回 <= 0 才会出队
 *  另外，由于 DelayQueue 内部委托了 PriorityQueue 来实现所有的方法，所以能以堆的结构维护元素顺序，这样剩余时间最小的元素就在堆顶，每次出队就是删除剩余时间 <= 0 的最小时间
 *
 *  DelayQueue 是无界队列
 *  队列中的元素必须实现 Delayed 接口，元素过期后才会从队列中取走
 *
 *  适用场景：缓存或定时任务的设计，以及异步通知的重试。失效时间可以根据已通知的次数来设定，如果超过一定阈值，把消息抛弃。
 *
 * 2. DelayQueue 源码分析
 *  2.1 属性
 *      private final transient ReentrantLock lock = new ReentrantLock();
 *      private final Condition available = lock.newCondition();
 *
 *      // PriorityQueue 维护队列
 *      private final PriorityQueue<E> q = new PriorityQueue<E>();
 *      private Thread leader = null;
 *
 *      比较特殊的 leader 字段，DelayQueue 每次只会出队一个过期元素，如果队首元素没有过期，就会阻塞出队线程，让线程在 available 这个条件队列上无限等待
 *      为了提升性能，DelayQueue 并不会让所有出队线程都无限等待，而是用 leader 保存了第一个尝试出队的线程，该线程的等待时间是队首元素的剩余有效期。这样，一旦 leader 线程被唤醒（队首元素失效），
 *      就可以出队成功，然后唤醒一个其他在 available 条件队列上等待的线程。之后，会重复上一步，新唤醒的线程可能取代称为新的 leader 线程。这样，就避免了无效的等待，提升了性能。这其实是一种名为
 *      Leader-Follower pattern 的多线程设计模式。
 *
 *  2.2 入队 offer
 *      public boolean offer(E e) {
 *      final ReentrantLock lock = this.lock;
 *      lock.lock();
 *      try {
 *          q.offer(e);             // 调用 PriorityQueue#offer 方法
 *          if (q.peek() == e) {    // 如果入队元素在队首, 则唤醒一个出队线程
 *              leader = null;
 *              available.signal();
 *          }
 *          return true;
 *      } finally {
 *          lock.unlock();
 *      }
 *  }
 *
 *      首次入队元素时，需要唤醒一个出队线程，因为此时可能已经有出队线程在空队列上等待了，如果不唤醒，会导致出队线程永远无法执行。
 *
 *  2.3 不阻塞出队 poll
 *      public E poll() {
 *     final ReentrantLock lock = this.lock;
 *     lock.lock();
 *     try {
 *         E first = q.peek();
 *         // 1. 没有元素或元素还在有效期内则直接返回 null
 *         if (first == null || first.getDelay(NANOSECONDS) > 0)
 *             return null;
 *         // 2. 元素已经失效直接取出来一个
 *         else
 *             return q.poll();
 *     } finally {
 *         lock.unlock();
 *     }
 * }
 *
 *  2.4 阻塞式出队 take
 *      public E take() throws InterruptedException {
 *     final ReentrantLock lock = this.lock;
 *     lock.lockInterruptibly();
 *     try {
 *         for (;;) {
 *             E first = q.peek();
 *             // 1. 集合为空时所有的线程都处于无限等待的状态。
 *             //    只要有元素将其中一个线程转为 leader 状态
 *             if (first == null)
 *                 available.await();
 *             else {
 *                 long delay = first.getDelay(NANOSECONDS);
 *                 // 2. 元素已经过期，直接取出返回
 *                 if (delay <= 0)
 *                     return q.poll();
 *                 first = null; // don't retain ref while waiting
 *                 // 3. 已经在其它线程设置为 leader，无限期等着
 *                 if (leader != null)
 *                     available.await();
 *                 // 4. 将 leader 设置为当前线程，阻塞当前线程（限时等待剩余有效时间）
 *                 else {
 *                     Thread thisThread = Thread.currentThread();
 *                     leader = thisThread;
 *                     try {
 *                         available.awaitNanos(delay);
 *                     } finally {
 *                         // 4.1 尝试获取过期的元素，重新竞争
 *                         if (leader == thisThread)
 *                             leader = null;
 *                     }
 *                 }
 *             }
 *         }
 *     } finally {
 *         // 5. 队列中有元素则唤醒其它无限等待的线程
 *         //    leader 线程是限期等待，每次 leader 线程获取元素出队，如果队列中有元素
 *         //    就要唤醒一个无限等待的线程，将其设置为限期等待，也就是总有一个等待线程是 leader 状态
 *         if (leader == null && q.peek() != null)
 *             available.signal();
 *         lock.unlock();
 *     }
 * }
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/20   14:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DelayQueueTest {

    public static void main(String[] args) {
        CustomDelayQueue queue = new CustomDelayQueue();
        queue.start();

        queue.sj("A", 5);
        queue.sj("B", 2);
        queue.sj("C", 4);
    }

    private static class CustomDelayQueue extends Thread {
        private DelayQueue<Wm> queue = new DelayQueue<>();
        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        public void sj(String name, int money) {
            Wm wm = new Wm(name, System.currentTimeMillis() + money * 10001);
            queue.add(wm);
            System.out.println(name + "开始上网，时间：" + format.format(new Date()) +
                    "，预计下机时间为：" + format.format(new Date(wm.getEndTime())));
        }

        public void xj(Wm wm) {
            System.out.println(wm.getName() + "下机，时间：" + format.format(new Date(wm.getEndTime())));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Wm wm = queue.take();
                    xj(wm);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Wm implements Delayed {

        private String name;
        private long endTime;
        private TimeUnit timeUnit = TimeUnit.SECONDS;

        public Wm(String name, long endTime) {
            this.name = name;
            this.endTime = endTime;
        }

        public String getName() {
            return name;
        }

        public long getEndTime() {
            return endTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return endTime - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            Wm wm = (Wm) o;
            return this.getDelay(timeUnit) - wm.getDelay(timeUnit) > 0 ? 1 : (getDelay(timeUnit) - wm.getDelay(timeUnit) < 0 ? -1 : 0);
        }
    }

}