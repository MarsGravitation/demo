package com.microwu.net.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   9:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SubReactor implements Runnable {

    private final Selector selector;
    /**
     * 注册开关
     */
    private boolean register = false;
    /**
     * 序号，也就是 Acceptor 初始化 SubReactor 时的下标
     */
    private int num;

    SubReactor(Selector selector, int num) {
        this.selector = selector;
        this.num = num;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println(num + " 号 SubReactor 等待注册中。。。");
            while (!Thread.interrupted() && !register) {
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatch((SelectionKey) iterator.next());
                    iterator.remove();
                }
            }
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if (r != null) {
            r.run();
        }
    }

    void registering(boolean register) {
        this.register = register;
    }
}