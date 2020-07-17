package com.microwu.concurrent.executor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 关于进制问题（这里以8 位进行演示）
 *  1. 原码：最高位为符号位，0 代表正数，1 代表负数
 *      127: 0111 1111
 *      -127: 1111 1111
 *  2. 反码：正数的反码与原码一致，负数的反码是对原码按位取反，只是最高位符号不变
 *      127: 0111 1111
 *      -127：1000 0000
 *  3. 正数的补码与原码一致，负数的补码是反码 + 1
 *      127：0111 1111
 *      -127: 1000 0001
 *
 *  补码的好处：如果计算机内部采用原码来表示数，既要实现加法，也要实现减法，可不可以只用一种类型的运算器来实现加和减
 *  化减为加：时钟一圈是360°，-30° 表示逆时针30°，与顺时针 330°是一样的
 *  计算机中类似 360°叫做模，他可以实现化减为加，本质上是将逸出的部分舍去
 *
 *  单字节 2^8 = 256，模为 256
 *  在无符号位的情况下：127 + 2 = 129
 *      0111 1111
 *      0000 0010
 *      ---------
 *      1000 0001
 *  这时，我们将最高位作为符号位，计算机数字均以补码来表示，则 1000 0001 的原码为减一后按位取反 1111 1111，也就是 -127
 *  也就是说，计算机里的 129 表示 -127
 *
 *  结论：负数的补码为 模减去概述的绝对值
 *      -5 = 256 - 5 = 251 = 1111 1011
 *      -128 = 256 - 128 = 1000 0000
 *
 *  但是正128 就会溢出了，故单字节表示的数字范围是 -128 - 127
 *  最后看一下补码是如何通过模的溢出舍弃操作来完成化减为加
 *
 *  16 - 5 = 16 + (-5) = 11
 *
 *      0001 0000
 *      1111 1011
 *      ---------
 *     10000 1011
 *
 * https://www.jianshu.com/p/36ec7a047f29
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/4   9:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RadixDemo {

    /**
     * 11100000 00000000 00000000 00000000
     */
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    /**
     * 32 - 3 = 29
     */
    private static final int COUNT_BITS = Integer.SIZE - 3;
    /**
     * 00011111 11111111 11111111 11111111
     */
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
    /**
     * -1: 11111111 11111111 11111111 11111110
     *  -1 << 29 = 11100000 00000000 00000000 00000000
     */
    private static final int RUNNING    = -1 << COUNT_BITS;
    /**
     * 00000000 00000000 00000000 00000000
     */
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    /**
     * 00100000 00000000 00000000 00000000
     */
    private static final int STOP       =  1 << COUNT_BITS;
    /**
     * 01000000 00000000 00000000 00000000
     */
    private static final int TIDYING    =  2 << COUNT_BITS;
    /**
     * 01100000 00000000 00000000 00000000
     */
    private static final int TERMINATED =  3 << COUNT_BITS;

    /**
     * 线程池状态
     *
     * 11100000 00000000 00000000 00000000 - RUNNING
     * 11100000 00000000 00000000 00000000
     * -----------------------------------
     * 11100000 00000000 00000000 00000000 - RUNNING
     *
     *
     * @param c
     * @return
     */
    private static int runStateOf(int c)     { return c & ~CAPACITY; }

    /**
     * 线程池线程数量
     *
     * 11100000 00000000 00000000 00000001
     * 00011111 11111111 11111111 11111111
     * -----------------------------------
     * 00000000 00000000 00000000 00000001
     *
     *
     * @param c
     * @return
     */
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(CAPACITY));
        System.out.println(Integer.toBinaryString(RUNNING));
        System.out.println(Integer.toBinaryString(SHUTDOWN));
        System.out.println(Integer.toBinaryString(STOP));
        System.out.println(Integer.toBinaryString(TIDYING));
        System.out.println(Integer.toBinaryString(TERMINATED));

        RadixDemo radixDemo = new RadixDemo();
        System.out.println(Integer.toBinaryString(radixDemo.ctl.get()));

        System.out.println(Integer.toBinaryString(runStateOf(radixDemo.ctl.get())));
        System.out.println(Integer.toBinaryString(radixDemo.ctl.get() + 1));
        System.out.println(Integer.toBinaryString(workerCountOf(radixDemo.ctl.get() + 1)));
    }
}