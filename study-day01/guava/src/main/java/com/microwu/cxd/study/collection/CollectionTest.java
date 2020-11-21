package com.microwu.cxd.study.collection;

import com.google.common.collect.ImmutableSet;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/19   16:37
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CollectionTest {

    /**
     * 不可变对象的优点：
     *  1. 安全
     *  2. 线程安全
     *  3. 内存利用率
     *  4. 可以被当做常量使用
     *
     *  如果不需要修改某个集合，或者希望某个集合保持不变时，不接受 null 值
     */
    public static final ImmutableSet<String> COLOR_NAMES = ImmutableSet.of("red", "orange");

    /**
     * 对于有序不可变集合来说，排序是在构建集合的时候完成的
     *
     * copyOf
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/11/19  16:46
     *
     * @param
     * @return  void
     */
    public static void test() {
        // 创建不可变集合
        // copy of
        ImmutableSet.copyOf(new String[]{});
        // of
        // builder 工具
        ImmutableSet.builder().add("a").build();
    }

    /**
     * 新集合类型
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/11/19  16:51
     *
     * @param
     * @return  void
     */
    public static void test02() {

    }

    public static void main(String[] args) {

    }

}