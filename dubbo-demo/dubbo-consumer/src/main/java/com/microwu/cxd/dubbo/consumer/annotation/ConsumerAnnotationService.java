package com.microwu.cxd.dubbo.consumer.annotation;

import com.alibaba.dubbo.config.annotation.Reference;
import com.microwu.cxd.dubbo.provider.annotation.ProviderServiceAnnotation;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/26   11:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class ConsumerAnnotationService {

    @Reference
    private ProviderServiceAnnotation providerServiceAnnotation;

    public String sayHello(String name) {
        return providerServiceAnnotation.sayHelloAnnotation(name);
    }

}