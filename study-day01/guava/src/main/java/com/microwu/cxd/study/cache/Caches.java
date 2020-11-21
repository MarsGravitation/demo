package com.microwu.cxd.study.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/17   15:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Caches {

    /**
     * 如果有合理的默认方法来加载或计算与键有关联的值？
     * 如果有，应当使用 CacheLoader
     * 如果没有，或者你想要覆盖默认的加载运算，同时保存 get-if-absent-compute，你应该在调用 get 传入一个 Callable 实例。
     *
     * 所有类型的 Guava Cache，不管有没有自动加载功能，都支持 get(K, Callable<V>) 方法。这个方法返回缓存中相应的值，或者用给定的 Callable 运算把结果加入缓存中。
     * 这个方法实现了“如果有就返回，否则运算、缓存、返回”
     *
     * 显示插入：
     *  put 覆盖给定键之前映射的值
     *
     * 缓存回收
     *  基于容量回收
     *  定时回收
     *  引用回收
     *
     * 显示清除
     * 移除监听器
     * 清理什么时候发生？
     *  不会自动执行清理和回收工作，也不会再某个缓存项过期后马上清理，会在写操作顺带做少量的维护工作或者读操作时做
     *  如果系统是高吞吐的，无需担心，反之可以自己维护线程，以固定的时间调用 cache.cleanUp
     *
     * 刷新：
     *  刷新和回收不一样，刷新表示为键加载新值，可以是异步的。在刷新操作时，缓存仍然可以向其他线程返回旧值
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/11/17  15:47
     *
     * @param
     * @return  void
     */
    public static void test() {
        LoadingCache<String, Object> graphs = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .removalListener()
                .build(
                        new CacheLoader<String, Object>() {
                            @Override
                            public Object load(String s) throws Exception {
                                return null;
                            }
                        }
                );

        try {
            // 要么返回以缓存的值
            // 要么使用 CacheLoader 向缓存原子的加载新值
            graphs.get("");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}