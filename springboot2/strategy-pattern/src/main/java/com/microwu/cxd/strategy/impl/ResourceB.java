package com.microwu.cxd.strategy.impl;

import com.microwu.cxd.strategy.Strategy;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/19   11:56
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class ResourceB implements Strategy {
    @Override
    public String getStrategy(String id) {
        System.out.println("================ B, getStrategy ================" + id);
        return id;
    }
}