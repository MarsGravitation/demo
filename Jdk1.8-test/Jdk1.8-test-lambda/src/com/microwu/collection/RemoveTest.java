package com.microwu.collection;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 删除测试
 *
 * https://blog.csdn.net/zcyzsy/article/details/82790381
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/1  9:53
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class RemoveTest {

    private static ArrayList<Integer> list = new ArrayList<>(5);

    static {
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
    }

    /**
     * 错误删除方式一 foreach
     *
     * ConcurrentModificationException
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/1     9:54
     *
     * @param
     * @return void
     */
    public static void errorTest() {

//        list.removeIf(i -> i == 3);
        for(Integer i : list) {
            if (i == 3) {
                list.remove(i);
            }
        }
    }
    
    /**
     * Iterator
     *
     * ConcurrentModificationException
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/1     10:00
     *
     * @param 
     * @return void
     */
    public static void errorTest02() {
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            if (next == 3) {
                list.remove(next);
            }
        }
    }

    /**
     * 下标遍历
     *
     * 存在的问题，size 会随着删除而改变
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/1     10:05
     *
     * @param
     * @return void
     */
    public static void errorTest03() {
        int size = list.size();
        for (int i = 0; i < list.size(); i++) {
            if (i == 1) {
                list.remove(i);
            }
        }

        System.out.println(list);
    }

    /**
     * 使用迭代器的 remove 方法
     *
     * 不要在 foreach 中 add/remove 元素，如果 remove 元素请使用 iterator 方法
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/1     10:10
     *
     * @param
     * @return void
     */
    public static void correctTest() {
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == 2) {
                iterator.remove();
            }
        }
        System.out.println(list);
    }

    /**
     * 倒序遍历删除
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/1     10:14
     *
     * @param
     * @return void
     */
    public static void correctTest02() {

        System.out.println(list);
    }

    public static void main(String[] args) {
//        errorTest();
//        errorTest02();
//        errorTest03();
        correctTest();
    }

}
