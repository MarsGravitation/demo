package com.microwu.cxd.queue.disruptor.blog;

import com.lmax.disruptor.EventFactory;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/25   14:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class OrderFactory implements EventFactory<Order> {
    @Override
    public Order newInstance() {
        return new Order();
    }
}