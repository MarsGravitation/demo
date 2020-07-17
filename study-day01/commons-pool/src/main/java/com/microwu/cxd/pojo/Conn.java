package com.microwu.cxd.pojo;

/**
 * Description: 假设这是一个建立 TCP 连接的对象，该对象的初始化时间平均是 500 ms，为了避免在程序中频繁创建 conn 对象，
 *          我们需要借助对象池管理 conn 对象实例
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   15:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Conn {

    /**
     * 记录对象的创建时间
     */
    private long createTime;

    public Conn() throws InterruptedException {
        Thread.sleep(500);
        createTime = System.currentTimeMillis();

    }

    public void report() {

    }

}