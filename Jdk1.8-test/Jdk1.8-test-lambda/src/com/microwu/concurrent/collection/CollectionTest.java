package com.microwu.concurrent.collection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Description: 同步类容器和并发类容器
 *  1. 同步类容器
 *      线程安全，如 Vector、HashTable 底层使用 synchronized 修饰，每次只有一个线程访问容器，但是不满足高并发的需求
 *      Vector 底层使用 synchronized 修饰，线程安全
 *      HashMap 底层没有使用 synchronized 修饰，可以使用 Collections.synchronizedMap 实现线程安全
 *   2. 并发类容器
 *      ConcurrentHashMap：
 *      ConcurrentSkipListMap：排序
 *      CopyOnWriteArrayList：替代 Vector
 *      ConcurrentLinkedQueue：高性能队列，无阻塞
 *      LinkedBlockingQueue：阻塞队列
 *
 *   2.1 ConcurrentMap
 *      容器内部使用 Segment 来表示不同部分，每个段是一个小的 HashTable，它们有自己的锁。只要多个修改操作发生在不同的段上，它们就可以兵法进行。
 *      把整体分成了 16 个段（Segment）。也就是最高支持 16 个线程并发修改操作。这也是在多线程场景时减少锁粒度从而降低锁竞争的一种方案。并且代码中
 *      大多共享变量使用 volatile 修饰，目的是第一时间获取修改的内容。
 *      ConcurrentMap 的实现：ConcurrentHashMap，ConcurrentSkipListMap（排序）
 *   2.2 Copy-On-Write
 *      写时复制的容器，用于读多写少的场景。往一个容器添加元素，不能直接往当前容器添加，而是先将当前容器 Copy，复制一个新的容器，然后往新的容器添加元素，
 *      添加完成之后，再将原容器的引用指向新的容器，这样的好处是可以进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以 CopyOnWrite 容器是一种
 *      读写分离的思想，读和写不同的容器。
 *   2.3 ConcurrentLinkedQueue 无阻塞队列
 *      适合高并发场景下的队列，通过无锁的方式，实现了高并发状态下的高性能，通常 ConcurrentLinkedQueue 性能好于 LinkedBlockingQueue。它是一个基于链接节点
 *      的无界线程安全队列。该队列的元素遵循先进先出的原则，且不允许 null 元素
 *      add 和 offer：添加元素
 *      poll 和 peek：取头元素节点，前者删除元素，后者不会。注意：没有元素时返回 null，不会阻塞队列
 *   2.4 BlockingQueue
 *      与 ConcurrentLinkedQueue 相比，BlockingQueue 是阻塞的，即 put 方法在队列满的时候会阻塞直到有队列成员被消费，take 方法在队列空的时候也会阻塞，直到队列不为空
 *                  抛出异常    特殊值/超时      阻塞
 *      插入          add          offer          put
 *      移除          remove       poll           take
 *
 *      阻塞利用的是条件，返回特殊值是互斥
 *    2.4.1 ArrayBlockingQueue
 *      基于数组的阻塞队列实现，内部维护了一个定长的数组，以便缓存队列中的数据对象。没有实现读写分离，也就意味着生产者和消费者不能完全并行，需要定义长度，可以指定先进先出，也叫有界队列
 *    2.4.2 LinkedBlockingQueue
 *      基于链表的阻塞队列实现，链表实现。实现了读写分离（读和写两个锁），从而实现生产和消费的完全并行，高效的处理并发数据，是一个无界队列
 *    2.4.3 SynchronousQueue
 *    2.4.4 PriorityBlockingQueue
 *      基于优先级的阻塞队列。其内部控制线程同步的锁采用的是公平锁，是一个无界队列
 *    2.4.5 DelayQueue
 *      带有延迟时间的队列，其中的元素只有当其指定的延时时间到了，才能够从队列中获取元素，也是一个无界队列。应用场景为缓存超时的数据进行移除，任务超时处理，空闲连接的关闭
 *
 * https://segmentfault.com/blog/ressmix_multithread?page=3
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/19   14:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CollectionTest {

    private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
    /**
     * 允许一个或多个线程等待
     */
    private static final CountDownLatch latch = new CountDownLatch(2);

    public static void test() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 1; i <= 100; i++) {
            queue.offer(i);
        }
        for (int i = 0; i < 2; i++) {
            pool.submit(() -> {
                Random random = new Random();
                while (!queue.isEmpty()) {
                    try {
                        Thread.sleep(random.nextInt(10) * 50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 可能为 null
                    System.out.println(Thread.currentThread().getName() + ":" + queue.poll());
                }
                latch.countDown();
            });
        }
        // 主线程等待，直到 latch.countDown = 0 才继续执行
        latch.await();

        pool.shutdown();
    }

    public static void test02() {
        PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
        queue.add(new Task(1));
        queue.add(new Task(6));
        queue.add(new Task(3));

        while (true) {
            if (queue.size() == 0) {
                break;
            }
            System.out.println(queue.poll());
        }

    }

    public static void test03() {
        DelayQueueTest delayQueueTest = new DelayQueueTest();
        delayQueueTest.start();

        delayQueueTest.shang("A", 5);
        delayQueueTest.shang("B", 2);
        delayQueueTest.shang("C", 4);
    }

    public static void main(String[] args) throws InterruptedException {
//        test();
//        test02();
        test03();
    }

    private static class Task implements Comparable {
        private int id;

        public Task(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Object o) {
            Task task = (Task) o;
            return this.id - task.id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }

    private static class WangMing implements Delayed {

        private String name;
        private long endTime;
        private TimeUnit timeUnit = TimeUnit.SECONDS;

        public WangMing(String name, long endTime) {
            this.name = name;
            this.endTime = endTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return endTime - System.currentTimeMillis();
        }

        public String getName() {
            return name;
        }

        public long getEndTime() {
            return endTime;
        }

        @Override
        public int compareTo(Delayed o) {
            WangMing wangMing = (WangMing) o;
            return this.getDelay(timeUnit) - wangMing.getDelay(timeUnit) > 0 ? 1 : -1;
        }
    }

    private static class DelayQueueTest extends Thread {
        private DelayQueue<WangMing> delayQueue = new DelayQueue<>();
        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        public void shang(String name, int money) {
            WangMing wangMing = new WangMing(name, System.currentTimeMillis() + money * 10000);
            delayQueue.add(wangMing);
            System.out.println(name + "开始上网，时间：" + format.format(new Date()) +
                    "，预计下机时间为：" + format.format(new Date(wangMing.getEndTime())));
        }

        public void xia(WangMing wangMing) {
            System.out.println(wangMing.getName() + "下机，时间：" + format.format(new Date(wangMing.getEndTime())));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    WangMing wangMing = delayQueue.take();
                    xia(wangMing);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}