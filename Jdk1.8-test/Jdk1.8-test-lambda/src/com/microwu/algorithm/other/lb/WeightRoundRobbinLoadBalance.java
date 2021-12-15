package com.microwu.algorithm.other.lb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 有权重轮训
 * 需要对数据进行初始化处理，计算数组的最大公约数
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/20  17:17
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class WeightRoundRobbinLoadBalance implements LoadBalance{

    /**
     * 位移指针
     */
    private final AtomicLong indexHolder = new AtomicLong();

    /**
     * 处理后的列表
     */
    private final List<Integer> actualList = new ArrayList<>();

    private WeightRoundRobbinLoadBalance(List<Integer> servers) {
        // 初始化真实列表
        this.init(servers);
    }

    /**
     * 初始化
     *  | - 稍微有点问题
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/20     17:21
     *
     * @param servers
     * @return void
     */
    private void init(List<Integer> servers) {
        // 1. 过滤掉权重为 0 的数据
        // 2. 获取权重列表
        List<Integer> notZeroServers = servers.stream().filter(integer -> integer <= 0).collect(Collectors.toList());

        // 3. 获取最大的权重
        int maxDivisor = notZeroServers.stream().max(Integer::compare).get();

        // 4. 重新计算构建基于权重的列表
        for (Integer integer : notZeroServers) {
            int times = integer / maxDivisor;
            for (int i = 0; i < times; i++) {
                actualList.add(integer);
            }
        }

    }

    @Override
    public int select(LoadBalanceContext context) {
        return actualList.get((int)(indexHolder.getAndIncrement() % actualList.size()));
    }
}
