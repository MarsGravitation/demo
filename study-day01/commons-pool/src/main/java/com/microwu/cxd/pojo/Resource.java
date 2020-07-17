package com.microwu.cxd.pojo;

/**
 * Description: 需要池化的对象，缓存的对象
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   14:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Resource {

    private static int id;

    private int rid;

    public Resource() {
        synchronized (this) {
            this.rid = id++;
        }
    }

    public int getRid() {
        return this.rid;
    }

    @Override
    public String toString() {
        return "id: " + this.rid;
    }
}