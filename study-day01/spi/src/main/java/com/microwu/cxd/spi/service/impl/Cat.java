package com.microwu.cxd.spi.service.impl;

import com.microwu.cxd.spi.service.IShout;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/18   15:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Cat implements IShout {
    @Override
    public void shout() {
        System.out.println("mi mi");
    }
}