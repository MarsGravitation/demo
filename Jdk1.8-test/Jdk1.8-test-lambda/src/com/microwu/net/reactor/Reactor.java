package com.microwu.net.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/6   15:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Reactor implements Runnable {

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws IOException { //Reactor初始化
        // 打开一个Selector
        selector = Selector.open();
        // 建立一个Server端通道
        serverSocketChannel = ServerSocketChannel.open();
        // 绑定服务端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        // selector模式下，所有通道必须是非阻塞的
        serverSocketChannel.configureBlocking(false);
        //Reactor 是入口，最初给一个channel注册上去的事件都是accept
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor
        // 绑定接收事件处理器
//        sk.attach(new Acceptor(serverSocketChannel, selector));
        sk.attach(new AsyncAcceptor(serverSocketChannel));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 就绪事件到达之前，阻塞
                selector.select();
                // 拿到本次select获取的就绪事件
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    //这里进行任务分发
                    dispatch((SelectionKey) (it.next()));
                }
                selected.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void dispatch(SelectionKey k) {
        // 这里很关键，拿到每次selectKey里面附带的处理对象，然后调用其run，这个对象在具体的Handler里会进行创建，初始化的附带对象为Acceptor（看上面构造器）
        Runnable r = (Runnable) (k.attachment());
        System.out.println(r.getClass());
        // 调用之前注册的callback对象
        if (r != null) {
            // 只是拿到句柄执行run方法，并没有新起线程
            r.run();
        }
    }
}