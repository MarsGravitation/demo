package com.microwu.cxd.service.service;

import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/10/25   15:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class SchedualServiceHiHystric implements SchedualServieHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry, " + name;
    }
}