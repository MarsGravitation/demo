package com.microwu.cxd.kafka.common;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/2   14:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Bar2 {

    public String bar;

    public Bar2() {
    }

    public Bar2(String bar) {
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
        return "Bar2 [bar=" + this.bar + "]";
    }

}