package com.microwu.cxd.design.rookie.abstract_factory;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:29
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Green implements Color {
    @Override
    public void fill() {
        System.out.println("充满绿色...");
    }
}