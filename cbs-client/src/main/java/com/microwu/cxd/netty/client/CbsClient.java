package com.microwu.cxd.netty.client;

import com.microwu.cxd.netty.core.Address;
import com.microwu.cxd.netty.core.ConnectionManager;
import com.microwu.cxd.netty.core.DefaultClientConnectionFactory;
import com.microwu.cxd.netty.core.DefaultConnectionManager;
import com.microwu.cxd.netty.struct.BcnMessage;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   8:29
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CbsClient {

    private ConnectionManager connectionManager;

    private CbsClientRemoting remoting;

    public void start() {
        this.connectionManager = new DefaultConnectionManager(new DefaultClientConnectionFactory());
        this.connectionManager.start();
        this.remoting = new CbsClientRemoting(connectionManager);
    }

    public void shutdown() {
        this.connectionManager.shutdown();
    }

    public void oneWay(Address address, BcnMessage message) {
        this.remoting.oneWay(address, message);
    }

}