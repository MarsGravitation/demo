package com.microwu.cxd.builder;

/**
 * Description:     食物条目
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   16:32
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface Item {
    public String name();
    public Packing packing();
    public float price();
}