package com.microwu.algorithm.other.lb;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮训策略
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/20  17:13
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class RoundRobbinLoadBalance implements LoadBalance {

    private int size;

    private final AtomicLong indexHolder = new AtomicLong();

    public RoundRobbinLoadBalance(int size) {
        this.size = size;
    }

    @Override
    public int select(LoadBalanceContext context) {
        long index = indexHolder.getAndIncrement();
        return (int)(index % size);
    }
}
