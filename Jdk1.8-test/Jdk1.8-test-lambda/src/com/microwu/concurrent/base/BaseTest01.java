package com.microwu.concurrent.base;

/**
 * Description:
 *  可见性、原子性、有序性问题：
 *      1.1 性能
 *      CPU，内存，硬盘的速度
 *      CPU 一天，内存一年
 *      内存一天，I/O 十年
 *
 *      根据木桶理论，程序整体的性能取决于最慢的操作 -- 读写 I/O 设备，也就是说单方面提高 CPU 性能是无效的。
 *      为了合理利用 CPU 的高性能，计算机体系结构，操作系统，编译程序都作出了贡献，主要体现为：
 *      可见性：CPU 增加了缓存
 *      原子性：操作系统增加了进程、线程，分时复用 CPU，进而均衡 CPU 与 I/O 设备的速度差异。但线程切换也带来了原子性问题
 *      有序性：编译程序优化指令执行次序，使得缓存能够合理利用，但也带来了有序性问题
 *
 *      2. 可见性
 *      一个线程对共享变量的修改，另一个线程能够立刻看到，我们称为可见性。CPU 缓存导致了可见性问题。
 *      单核时代，所有的指令都是运行在同一个 CPU 上，不会存在缓存不一致的问题。但到了多核心时代，线程 A 操作 CPU-1 上的缓存，而线程
 *      B 操作 CPU-2 的缓存。这时线程 A 对变量的操作对于线程 B 就不可见了
 *
 *      3. 原子性
 *      一个或多个操作在 CPU 执行过程中不会被中断的特性，称为原子性。CPU 能保证的原子操作时 CPU 指令级别的，而不是高级语言的操作符。
 *      线程切换导致了原子性问题。
 *
 *      比如 count++ 至少需要三条 CPU 指令，所以自增操作不是原子性的
 *
 *      4. 有序性
 *      编译器为了优化性能，有时候会改变程序中语句的先后顺序。Java 中提供了 happens-before 来解决
 *
 *
 * https://www.cnblogs.com/binarylei/p/12533857.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/16   9:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BaseTest01 {
}