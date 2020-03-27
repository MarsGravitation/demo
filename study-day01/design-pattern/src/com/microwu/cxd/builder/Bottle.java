package com.microwu.cxd.builder;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   16:45
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