package com.microwu.concurrent.future;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/24   10:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LocalCacheConnection extends AbstractLocalCache<String, Connection> {
    @Override
    public Connection computeV(String s) {
        System.out.println("创建 Connection 开始。。。");
        System.out.println("睡觉开始。。。");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("睡觉结束。。。");
        System.out.println("创建 Connection 结束。。。");

        return new Connection() {
            @Override
            public String getName() {
                return "A connection(" + s + ")";
            }
        };
    }
}