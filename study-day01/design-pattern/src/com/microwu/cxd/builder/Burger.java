package com.microwu.cxd.builder;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   16:46
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public abstract class Burger implements Item {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

    @Override
    public abstract float price();
}