package com.microwu.cxd.netty.core;

/**
 * Description: 连接工厂，创建 Connection 实例，检查 Connection
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   8:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface ConnectionFactory {

    void init();

    Connection create(Address address);
}