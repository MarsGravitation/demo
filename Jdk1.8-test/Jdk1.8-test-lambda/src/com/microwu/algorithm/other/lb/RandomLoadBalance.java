package com.microwu.algorithm.other.lb;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机策略
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/20  17:10
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class RandomLoadBalance implements LoadBalance {

    private int size;

    public RandomLoadBalance(int size) {
        this.size = size;
    }

    @Override
    public int select(LoadBalanceContext context) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(size);
    }
}
