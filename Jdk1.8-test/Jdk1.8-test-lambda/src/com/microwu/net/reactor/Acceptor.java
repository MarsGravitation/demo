package com.microwu.net.reactor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/6   15:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Acceptor implements Runnable {

    private final Selector selector;

    private final ServerSocketChannel serverSocketChannel;

    public Acceptor (ServerSocketChannel serverSocketChannel, Selector selector) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {
        SocketChannel socketChannel;
        try {
            // 三次握手
            socketChannel = serverSocketChannel.accept();
            System.out.println("收到来自客户端的连接 。。。");

            // 这里把客户端通道传给 handler
//            new Handler(socketChannel, selector);
            new AsyncHandler(socketChannel, selector);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}