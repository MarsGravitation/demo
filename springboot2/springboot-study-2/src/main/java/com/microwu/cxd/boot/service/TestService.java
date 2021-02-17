package com.microwu.cxd.boot.service;

import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/27   11:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class TestService {

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}