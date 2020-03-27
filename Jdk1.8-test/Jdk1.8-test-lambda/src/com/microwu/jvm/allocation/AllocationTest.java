package com.microwu.jvm.allocation;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/9   11:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AllocationTest {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        test03();

    }

    /**
     * 对象优先分配在Eden 区
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/1/9  11:46
     *
     * @param
     * @return  void
     */
    public static void test() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        // 出现一次 Minor GC
        allocation4 = new byte[4 * _1MB];
    }

    /**
     * 大对象直接进入老年代
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/1/9  11:47
     *
     * @param
     * @return  void
     */
    public static void test02() {
        byte[] allocation = new byte[4 * _1MB];
    }

    /**
     * 长期存活的对象进入老年代
    *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/1/9  11:53
     *
     * @param
     * @return  void
     */
    public static void test03() {
        byte[] a1, a2, a3;
        a1 = new byte[_1MB / 4];
        a2 = new byte[4 * _1MB];
        a3 = new byte[4 * _1MB];
        a3 = null;
        a3 = new byte[4 * _1MB];
    }

}