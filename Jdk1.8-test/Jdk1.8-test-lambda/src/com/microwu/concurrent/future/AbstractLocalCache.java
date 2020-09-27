package com.microwu.concurrent.future;

import java.util.concurrent.*;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/24   10:24
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public abstract class AbstractLocalCache<K, V> {

    private ConcurrentHashMap<K, Future<V>> pool = new ConcurrentHashMap<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    // 模版方法
    public abstract V computeV(K k);

    public Future<V> getResult(K k){
        if(pool.containsKey(k)){
            return pool.get(k);
        }
        FutureTask<V> future = new FutureTask<V>(new Callable<V>() {
            @Override
            public V call() throws Exception {
                return computeV(k);
            }
        });

        // 说明map中以前没有对应的 futureTask
        // 仔细体会 putIfAbsent 的作用
        if(pool.putIfAbsent(k, future) == null){
            executorService.submit(future);
        }
        return future;
    }

}