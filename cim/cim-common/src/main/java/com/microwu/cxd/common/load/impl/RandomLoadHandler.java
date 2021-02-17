package com.microwu.cxd.common.load.impl;

import com.microwu.cxd.common.load.LoadHandler;
import com.microwu.cxd.common.utils.CollectionUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   14:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RandomLoadHandler implements LoadHandler {
    @Override
    public String load(List<String> values, String key) {
        CollectionUtil.isEmpty(values);
        int offset = ThreadLocalRandom.current().nextInt(values.size());
        return values.get(offset);
    }
}