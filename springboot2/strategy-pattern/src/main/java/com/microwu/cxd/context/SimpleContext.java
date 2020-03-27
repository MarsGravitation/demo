package com.microwu.cxd.context;

import com.microwu.cxd.strategy.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:    通过Spring将实现的Strategy的实现类都自动注入到strategyMap类中, 当用户传入选择的
 *                 资源池时, 自动根据资源池id去对应的资源池实现中查找资源
 *
 *                 https://blog.csdn.net/qq_42684642/article/details/88577535
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/19   11:58
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class SimpleContext {
    @Autowired
    private final Map<String, Strategy> strategyMap = new ConcurrentHashMap<>(2);

    public SimpleContext( Map<String, Strategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v) -> this.strategyMap.put(k, v));

    }

    public Strategy getResource(String id) {
        return this.strategyMap.get(id);
    }
}