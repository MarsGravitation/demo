package com.microwu.cxd.kafka.common;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/2   14:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Bar1 {
    public String bar;

    public Bar1() {
    }

    public Bar1(String bar) {
        this.bar = bar;
    }

    public String getBar() {
        return this.bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    @Override
    public String toString() {
        return "Bar1 [bar=" + this.bar + "]";
    }
}