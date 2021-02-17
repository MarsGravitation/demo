package com.microwu.cxd.zookeeper.common;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/2   13:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CommonTest {

    @Test
    public void test() throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
    }

}