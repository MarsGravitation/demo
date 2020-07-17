package com.microwu.cxd.pool;

import com.microwu.cxd.config.ConnPoolConfig;
import com.microwu.cxd.factory.ConnFactory;
import com.microwu.cxd.pojo.Conn;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   15:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConnPool extends GenericObjectPool<Conn> {

    /**
     * 调用 GenericObjectPool 的构造方法，构造 ConnPool
     */
    public ConnPool() {
        super(new ConnFactory(), new ConnPoolConfig());
    }

    /**
     * 调用 GenericObjectPool 的构造方法，构造 ConnPool
     */
    public ConnPool(ConnPoolConfig connPoolConfig) {
        super(new ConnFactory(), connPoolConfig);
    }
}