package com.microwu.cxd.design.builder;

/**
 * Description:    食物条目
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/7/23   14:55
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface Item {
    String name();
    Packing packing();
    float price();

}
