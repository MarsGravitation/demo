package com.microwu.cxd.abstract_factory;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   14:20
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Red implements Color {
    @Override
    public void fill() {
        System.out.println("红色填充。。。");
    }
}