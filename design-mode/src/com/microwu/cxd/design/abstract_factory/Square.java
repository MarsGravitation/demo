package com.microwu.cxd.design.abstract_factory;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:02
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Square implements Shape {
    @Override
    public void draw() {
        System.out.println("画一个正方形的方法...");
    }
}