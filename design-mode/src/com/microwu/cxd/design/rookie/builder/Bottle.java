package com.microwu.cxd.design.rookie.builder;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:58
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Bottle implements Packing {
    @Override
    public String pack() {
        return "Bottle";
    }
}