package com.microwu.algorithm.other.lb;

/**
 * 普通哈希
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/20  17:29
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CommonHashLoadBalance implements LoadBalance {

    private int size;

    public CommonHashLoadBalance(int size) {
        this.size = size;
    }

    @Override
    public int select(LoadBalanceContext context) {
        int hashCode = context.hashCode();
        return hashCode % size;
    }
}
