package com.microwu.cxd.design.rookie.abstract_factory;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:03
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("画一个圆形的方法...");
    }
}