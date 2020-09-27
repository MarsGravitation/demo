package com.microwu.net.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   9:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AsyncAcceptor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;

    /**
     * 获取 CPU 核心数
     */
    private final int coreNum = Runtime.getRuntime().availableProcessors();

    /**
     * 创建 Selector 给 SubReactor 使用，个数为 CPU 核心数
     */
    private final Selector[] selectors = new Selector[coreNum];

    /**
     * 轮询使用 SubReactor 的下标索引
     */
    private int next = 0;

    private SubReactor[] subReactors = new SubReactor[coreNum];

    private Thread[] threads = new Thread[coreNum];

    AsyncAcceptor(ServerSocketChannel serverSocketChannel) throws IOException {
        this.serverSocketChannel = serverSocketChannel;
        // 初始化
        for (int i = 0; i < coreNum; i++) {
            selectors[i] = Selector.open();
            // 初始化 SubReactor
            subReactors[i] = new SubReactor(selectors[i], i);
            // 初始化运行 sub reactor 的线程
            threads[i] = new Thread(subReactors[i]);
            // 启动
            threads[i].start();
        }
    }

    @Override
    public void run() {
        SocketChannel socketChannel;
        try {
            // 阻塞获取连接
            socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                // 轮询 reactors 处理请求
                System.out.println("收到来自 " + socketChannel.getRemoteAddress() + " 的连接。。。");
                socketChannel.configureBlocking(false);
                /**
                 * 让下一次的 subReactors 的 while 循环不去执行
                 * 下一步需要执行 wakeup
                 */
                subReactors[next].registering(true);

                // 使一个阻塞住的 Selector 操作立即返回
                selectors[next].wakeup();

                // 当前客户端通道，向 Selector 注册一个读事件，返回key
                SelectionKey selectionKey = socketChannel.register(selectors[next], SelectionKey.OP_READ);

                // 使一个阻塞住的 Selector 操作立即返回，这样才能对刚刚注册的 selectionKey 感兴趣
                selectors[next].wakeup();

                // 本次事件注册完成后，需要再次触发 select 的执行
                subReactors[next].registering(false);
                // 绑定 handler
                selectionKey.attach(new AsyncHandler(socketChannel, selectors[next]));

                // 轮询负载
                if (++next == selectors.length) {
                    next = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}