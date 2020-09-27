package com.microwu.cxd.dubbo;

import com.alibaba.dubbo.rpc.RpcContext;
import org.apache.dubbo.config.annotation.Service;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/4   15:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name + ", response from provider: " + RpcContext.getContext().getLocalAddress();
    }
}