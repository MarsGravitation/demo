package com.microwu.cxd.jvm;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 一个对象在 new 出来之后在内存中主要分为 4 个部分：
 *  1. markword 这部分其实就是加锁的核心，同时还包括对象的一些生命周期，例如是否 GC、
 *  经过了几次 Young GC 还存活
 *  2. klass pointer 记录了指向对象的 class 文件指针
 *  3. instance data 记录了对象里面的变量数据
 *  4. padding 作为对齐使用，对象在 64 为服务器版本中，规定对象内存必须要能被 8 字节整除，如
 *  果不能整除，那么就靠对齐来补。举个例子，new 除了一个对象，内存只占用 18 字节，但是
 *  规定要能被 8 整除，所以 padding = 6
 *
 *
 *   OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
 *       0     4        (object header)                           70 f1 bf dc (01110000 11110001 10111111 11011100) (-591400592)
 *       4     4        (object header)                           ff 00 00 00 (11111111 00000000 00000000 00000000) (255)
 *       8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
 *       // 对象头：markword + klass
 *      12     4        (loss due to the next object alignment)
 *      // 由于下一个对象对齐而造成的损失
 * Instance size: 16 bytes // Object 对象占 16 个字节
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 *  a. 对象头包含了 12 字节，分成了 3 行，其中前 2 行就是 markword，第三行就是 klass 指针。值得
 *  注意的是在加锁前后输出从 001 变成了 000. Markword 用处：8 字节（64bit）的头记录一些信息，锁
 *  就是修改了 markword 的内容 8 字节（64 bit）的头记录一些信息。从 001 无锁状态，变成 000 轻量级
 *  锁状态。
 *
 *  b. new 出一个 object  对象，占用 16 字节。对象头占用 12 字节，由于 Object 中没有额外的变量，
 *  所以 instance = 0，考虑要对象内存大小要被 8 字节整除，那么 padding = 4，最后 new Object()
 *  内存大小为 16 字节。
 *
 *  拓展：什么样的对象会进入老年代？很多场景例如对象太大了可以直接进入，但是这里想探讨的是
 *  为什么从 Young GC 的对象最多经历 15 次 Young GC 还存活就会进入 Old 区（年龄是可以调的，默
 *  认是 15）。上图中 hotspots 的 markword 的图中，用了 4 个 big 去表示分层年龄，那么能表示的最大
 *  范围就是 0 - 15.所以这就是为什么设置新生代的年龄不能超过 15.
 *
 *  sleep 5s:
 *
 *  java.lang.Object object internals:
 *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
 *       0     4        (object header)                           05 50 75 9e (00000101 01010000 01110101 10011110) (-1636478971)
 *       4     4        (object header)                           74 02 00 00 (01110100 00000010 00000000 00000000) (628)
 *       8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
 *      12     4        (loss due to the next object alignment)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 * x86 + windows 一般采用的是小端，所以第一个字节是最后三位是锁的信息
 *
 * 问题 1：为什么要进行锁升级？
 * 问题 2：为什么要有偏向锁？
 *  统计表示，我们日常用的 syn 锁过程中，70 - 80 % 的情况下，一般都只有一个线程去拿锁，
 *  虽然底层加了 syn 锁，但是基本没有多线程竞争。那么这种情况下，没有必要升级到轻量级锁级别。
 *  偏向的意义在于：第一个线程拿到锁，将自己的线程信息标记在锁上，下次进来就不需要再去拿锁
 *  验证了。如果超过 1 个线程去抢锁，那么偏向锁就会撤销，升级为轻量级锁，其实我认为严格意义
 *  上来讲偏向锁并不算一把真正的锁，因为只有一个线程去访问共享资源的时候才会有偏向锁这个情况。
 *
 * 问题 3：为什么 jdk8 要在 4s 后开启偏向锁？
 *  明确知道在刚开始执行代码时，一定有很多线程来抢锁，如果开了偏向锁效率反而降低，所以上面
 *  程序在睡了 5s 之后偏向锁才开放。因为中途多了几个额外的过程，上了偏向锁之后多个线程争抢
 *  共享资源的时候要进行锁升级升级到轻量级锁，这个过程还得把偏向锁进行撤销再进行升级，所以
 *  导致效率会降低。
 *
 * 锁的升级流程
 *  对象从创建出来之后近内存从无锁状态 -> 偏向锁（如果开启了） -> 轻量级锁 -> 重量级锁
 *
 *  什么叫轻量级锁，从一个线程抢占资源（偏向锁）到多线程抢占资源升级为轻量级锁，线程如果
 *  没那么多的话，其实这里就可以理解为 CAS，也就是我们说的 Compare and Swap。
 *
 *
 public final int getAndIncrement() {
 return unsafe.getAndAddInt(this, valueOffset, 1);
 }

 public final int getAndAddInt(Object var1, long var2, int var4) {
 int var5;
 do {
 var5 = this.getIntVolatile(var1, var2);
 } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

 return var5;
 }
 * 问题 4：什么情况下轻量级锁要升级为重量级锁？
 *
 *  首先我们可以思考的是多个线程的时候先开启轻量级锁，如果它 carry 不了的情况下才会升级为重量级。
 *  1. 如果线程数太多，比如上来就是 10000 个，那么这里 CAS 要转多久才可能交换值，同时 CPU 光
 *  在这 10000 个活着的线程中来回切换中就耗费了巨大的资源，这种情况下自然就升级为重量级锁，直接
 *  交给操作系统入队管理，那么就算 10000 个线程那也是处理休眠的情况等待排队唤醒。
 *  2. CAS 如果自旋 10 次依然没有获取到锁，那么也会升级为重量级锁
 *
 *  总的来说 2 种情况慧聪轻量级升级为重量级，10 次自旋或等待 CPU 调度数超过 CPU 核数的一半，自动
 *  升级为重量级锁。输入 top 指令，然后按 1 可以看到 CPU 的核数
 *
 * 问题 5：都说 syn 为重量级锁，那么到底重在哪里？
 *
 *  JVM 把任何跟线程有关的操作全部交给操作系统去做，例如调度锁的同步直接交给操作系统去执行，而在操作
 *  系统中要先入队，另外操作系统启动里一个线程时需要消耗很多资源，消耗资源比较重。
 *
 * https://mp.weixin.qq.com/s?__biz=MzIzOTU0NTQ0MA==&mid=2247504001&idx=1&sn=f6ed553b6b18a169482363177a3e1dee&chksm=e92aed8ede5d6498a1e2d5887d1e684f473e70496c5f5569e504cf91028da83f926f6439739a&scene=178&cur_album_id=1391790902901014528#rd
 *
 * Java 中的那些锁
 *
 *  乐观锁和悲观锁
 *      synchronized 和 ReentrantLock 是悲观锁
 *      CAS 属于乐观锁
 *
 *      乐观锁适用于写比较少，冲突比较少的场景，因为不用上锁，释放所，省去了锁的开销，从而提升了吞吐量。
 *      如果是写多读少，冲突比较严重，线程间竞争激烈，使用乐观锁就是导致线程不断进行重试，可能还降低了
 *      性能，这种场景下使用悲观锁比较合适
 *
 *  独占锁和共享锁
 *
 *      独占锁是指锁一次只能被一个线程所持有。如果一个线程对数据加上了排它锁，那么其他线程不能再对该数据
 *      加任何类型的锁。获取独占锁的线程技能读取数据又能修改数据。
 *      synchronized 和 JUC 的 Lock 就是独占锁
 *
 *      共享所是指锁可被多个线程所持有。如果一个线程对数据加上了共享锁后，那么其他线程只能对数据再加共享锁，
 *      不能加独占锁。获得共享锁的线程只能读取数据，不能修改数据。
 *      ReentrantReadWriteLock 是一种共享锁
 *
 *  互斥锁和读写锁
 *      互斥锁是独占锁的一种常规实现，是指某一资源同时只允许一个访问者对齐进行访问，具有唯一性和排他性。
 *      互斥锁一次只能有一个线程拥有互斥锁，其他线程只有等待。
 *
 *      读写锁是共享锁的一种实现。读写锁管理一组锁，一个是只读的锁，一个是写锁。
 *      读锁可以在没有写锁的时候被多个线程同时持有，而写锁是独占的。写锁的优先级要高于读锁，一个获取了读锁的
 *      线程必须能看到前一个释放的写锁所更新的内容。
 *
 *      读写锁相比于互斥锁并发程度跟高，每次只有一个写线程，但是同时可以有多个线程并发读。
 *
 *  公平锁和非公平锁
 *      公平锁是指多个线程按照申请锁的顺序来获取锁
 *      非公平锁是指多个线程获取锁的顺序并不是按照申青锁的顺序
 *
 *      synchronized 是非公平锁，ReentrantLock 默认是非公平锁
 *
 *  可重入锁
 *      可重入锁，是指同一个线程在外层方法获取了锁，在进入内层方法会自动获取锁。
 *      ReentrantLock 和 Synchronized 是可重入锁。可重入锁的一个好处是可一定程度避免死锁。
 *
 *  自旋锁
 *      是指线程在没有获取锁时不是被直接挂起，而是执行一个忙循环。自旋锁的目的就是为了减少线程被挂起的几率，
 *      因为线程的挂起和唤醒都是耗资源的操作。
 *
 *      如果锁被一个线程占用的时间比较长，即是自旋了之后当前线程还是会被挂起，忙循环就会变成浪费系统资源的操作，
 *      反而降低了整体性能。因此自旋锁是不适应锁占用时间长的并发情况。
 *
 * public final int getAndAddInt(Object o, long offset, int delta) {
 *     int v;
 *     do {
 *         v = getIntVolatile(o, offset);
 *     } while (!compareAndSwapInt(o, offset, v, v + delta));
 *     return v;
 * }
 *
 *      CAS 操作如果失败就会一直循环获取当前 value 然后重试。
 *
 *      JDK 1.6 引入了自适应自旋，由前一次在同一个锁上的自旋时间以及锁的拥有者者的状态来决定。
 *
 *  分段锁
 *      设计的目的是将锁的粒度进一步细化
 *
 *  锁升级
 *  锁优化（锁粗化、锁消除）
 *
 * https://mp.weixin.qq.com/s/X_7KJXORH65wRMnrDIX0jQ
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/7/1  10:23
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class JolDemo {

    private static Object o;

    public static void main(String[] args) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        o = new Object();
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

}
