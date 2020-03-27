package com.microwu.cxd.properties;

import com.microwu.cxd.domain.Order;

/**
 * Description: 自定义 监听器适配器
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   20:52
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MessageDelegate {
    /**
     * 名称是固定的, 具体愿意看源码
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/14  20:53
     *
     * @param   	bytes
     * @return  void
     */
    public void handleMessage(byte[] bytes) {
        System.out.println("自定义适配器1: " + new String(bytes));

    }

//    public void consumerMessage(Map map) {
//        System.out.println("json: " + map);
//    }

    public void consumerMessage(Order order) {
        System.out.println("order: " + order.toString());
    }
}