package com.microwu.concurrent.local;

/**
 * Description:
 *  ThreadLocal 中的哈希魔数是 1640531527，十六进制 0x61c88647
 *
 *  哈希算法：keyIndex = ((i + 1) * HASH_INCREMENT) & (length - 1)
 *  i：ThreadLocal 实例个数
 *  HASH_INCREMENT：哈希魔数
 *  length：ThreadLocalMap 中可容纳的 Entry 的个数
 *
 *  在 ThreadLocal 中的内部类 ThreadLocalMap 的初始化容量为 16，扩容后总是 2 的幂次方
 *
 *  每组元素经过散列算法后恰好填充满了整个容器，实现了完美散列
 *
 *  3 2 1 0
 *  7 14 5 12 3 10 1 8 15 6 13 4 11 2 9 0
 *  7 14 21 28 3 10 17 24 31 6 13 20 27 2 9 16 23 30 5 12 19 26 1 8 15 22 29 4 11 18 25 0
 *
 * https://www.throwx.cn/2019/02/17/java-concurrency-threadlocal-source-code/
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/1   10:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HashIncrementDemo {

    private static final int HASH_INCREMENT = 0x61c88647;

    private static void hashCode(int capacity) {
        int keyIndex;
        for (int i = 0; i < capacity; i++) {
            keyIndex = ((i + 1) * HASH_INCREMENT) & (capacity - 1);
            System.out.print(keyIndex + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        hashCode(4);
        hashCode(16);
        hashCode(32);
    }

}