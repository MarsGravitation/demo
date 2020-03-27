package com.microwu.cxd.simple_factory;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   14:06
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}