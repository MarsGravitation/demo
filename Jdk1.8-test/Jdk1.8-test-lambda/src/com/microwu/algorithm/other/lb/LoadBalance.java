package com.microwu.algorithm.other.lb;

/**
 * 负载均衡算法
 *
 * https://houbb.github.io/2020/06/19/load-balance-03-hand-write
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/20  17:07
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public interface LoadBalance {

    int select(LoadBalanceContext context);

}
