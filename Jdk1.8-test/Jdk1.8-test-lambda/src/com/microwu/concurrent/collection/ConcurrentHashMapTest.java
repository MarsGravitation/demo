package com.microwu.concurrent.collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/4/4   16:23
 * Update History:
 * Author        Time            Content
 */
public class ConcurrentHashMapTest {

    private static final ConcurrentMap<Integer, Object> MAP = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        // null
        // aaa
        // aaa
        // 返回 null 表示第一次，返回不为 null，表示已经存在
        System.out.println(MAP.putIfAbsent(1, "aaa"));
        System.out.println(MAP.putIfAbsent(1, "bbb"));
        System.out.println(MAP.get(1));
    }

}
