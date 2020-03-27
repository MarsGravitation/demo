package com.microwu.cxd.nio;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/20   14:07
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Main {
    public static void main(String[] args) {
        BigFileReader.Builder builder = new BigFileReader.Builder("E:\\Project\\dp-server\\data\\a.txt", new IHandle() {
            @Override
            public void handle(String line) {
                System.out.println(line);
            }
        });
        builder.withBufferSize(10).withCharset("gbk").withBufferSize(1024 * 1024);
        BigFileReader bigFileReader = builder.build();
        bigFileReader.start();
    }
}