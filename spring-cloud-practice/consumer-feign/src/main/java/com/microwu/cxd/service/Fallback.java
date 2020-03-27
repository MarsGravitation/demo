package com.microwu.cxd.service;

import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/3/21   14:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Fallback implements HelloService {
    @Override
    public String hello() {
        return "failure";
    }
}