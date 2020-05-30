package com.microwu.cxd.dubbo.provider.annotation;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/26   11:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class ProviderServiceImplAnnotation implements ProviderServiceAnnotation {
    public String sayHelloAnnotation(String world) {
        return world;
    }
}