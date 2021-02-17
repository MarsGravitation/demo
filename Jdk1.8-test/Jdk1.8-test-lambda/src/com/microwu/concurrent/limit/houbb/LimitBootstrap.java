package com.microwu.concurrent.limit.houbb;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/29   10:04
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LimitBootstrap {

    private Limit limit;

    private long interval;

    private long count;

    private Class<?> clazz;

    private LimitBootstrap() {

    }

    public static LimitBootstrap newInstance() {
        return new LimitBootstrap();
    }

    public LimitBootstrap interval(long interval) {
        this.interval = interval;
        return this;
    }

    public LimitBootstrap count(long count) {
        this.count = count;
        return this;
    }

    public LimitBootstrap clazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public LimitBootstrap build() {

        return this;
    }

}