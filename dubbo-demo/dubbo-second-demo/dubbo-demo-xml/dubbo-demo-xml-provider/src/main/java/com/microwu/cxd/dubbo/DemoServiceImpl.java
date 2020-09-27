package com.microwu.cxd.dubbo;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/4   14:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DemoServiceImpl implements DemoService {


    @Override
    public String sayHello(String name) {
        return "Hello " + name + ", response from provider: " + RpcContext.getContext().getLocalAddress();
    }
}