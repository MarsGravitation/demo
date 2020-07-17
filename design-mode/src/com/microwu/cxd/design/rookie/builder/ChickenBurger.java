package com.microwu.cxd.design.rookie.builder;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   15:01
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ChickenBurger extends Burger {
    @Override
    public String name() {
        return "Chicken Burger";
    }

    @Override
    public float price() {
        return 50.5f;
    }
}