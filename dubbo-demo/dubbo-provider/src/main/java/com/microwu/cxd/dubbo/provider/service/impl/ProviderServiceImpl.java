package com.microwu.cxd.dubbo.provider.service.impl;

import com.microwu.cxd.dubbo.provider.service.ProviderService;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/21   10:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ProviderServiceImpl implements ProviderService {
    public String sayHello(String world) {
        return "hello " + world;
    }
}