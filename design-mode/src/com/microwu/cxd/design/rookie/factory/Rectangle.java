package com.microwu.cxd.design.rookie.factory;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:01
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("画一个长方形的方法...");
    }
}