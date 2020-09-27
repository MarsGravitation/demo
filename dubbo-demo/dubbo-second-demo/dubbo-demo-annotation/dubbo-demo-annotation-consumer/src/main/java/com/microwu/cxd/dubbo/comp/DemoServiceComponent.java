package com.microwu.cxd.dubbo.comp;

import com.microwu.cxd.dubbo.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/4   15:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class DemoServiceComponent implements DemoService {

    @Reference
    private DemoService demoService;

    @Override
    public String sayHello(String name) {
        return demoService.sayHello(name);
    }
}