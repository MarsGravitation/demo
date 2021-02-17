package com.microwu.cxd.common.load.impl;

import com.microwu.cxd.common.load.LoadHandler;
import com.microwu.cxd.common.utils.CollectionUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   13:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LoopLoadHandler implements LoadHandler {

    private AtomicInteger index = new AtomicInteger();

    @Override
    public String load(List<String> values, String key) {
        CollectionUtil.isEmpty(values);
        int position = index.getAndIncrement() % values.size();
        return values.get(position);
    }
}