package com.microwu.cxd.study.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/8  17:40
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CaffeineTest {

    private static final Cache<String, String> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .expireAfterAccess(1,TimeUnit.SECONDS)
            .maximumSize(10)
            .build();

    /**
     * 使用基本和 Guava 差不多，主要测试一个 get 方法
     * get 的区别在于 Guava 是一个 Callable 接口，Caffeine 是一个 Function
     *
     * 以下两种方式结果一样，它会把前面的当作入参
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/8     17:42
     *
     * @param
     * @return void
     */
    public static void test() {
        System.out.println(CACHE.get("a", CaffeineTest::get));
        System.out.println(CACHE.get("b", CaffeineTest::get));

        final String one = "1";
        final String two = "2";
        System.out.println(CACHE.get("a", k -> get("1")));
        System.out.println(CACHE.get("b", k -> get("2")));
    }

    public static void main(String[] args) {
        test();
    }

    public static String get(String s) {
        return s;
    }

}
