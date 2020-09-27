package com.microwu.net.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/6   16:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Handler implements Runnable {

    private final SelectionKey selectionKey;

    private final SocketChannel socketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);

    private final static int READ = 0;
    private final static int SEND = 1;

    private int status = READ;

    public Handler (SocketChannel socketChannel, Selector selector) throws IOException {
        // 接受客户端连接
        this.socketChannel = socketChannel;
        // 设置为非阻塞模式
        socketChannel.configureBlocking(false);

        // 将该客户端注册到 Selector，得到一个 selectionKey，以后的 select 到的就绪动作全都是由该对象进行封装
        selectionKey = socketChannel.register(selector, 0);
        // 附加处理对象， 当前是 handler 对象，run 是对象处理业务的方法
        selectionKey.attach(this);
        // 走到这里说明 Accept 的连接已经完成，接下来就是读取动作
        // 首先将读事件标记为感兴趣事件
        selectionKey.interestOps(SelectionKey.OP_READ);
        // 让阻塞的 selector 立即返回
        selector.wakeup();
    }
    @Override
    public void run() {
        try {
            switch (status) {
                case READ:
                    read();
                    break;
                case SEND:
                    send();
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() throws IOException {
        if (selectionKey.isValid()) {
            readBuffer.clear();
            int count = socketChannel.read(readBuffer);
            System.out.println("客户端消息：" + new String(readBuffer.array()));
            if (count > 0) {
                status = SEND;
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            }
        }
    }

    private void send() throws IOException {
        if (selectionKey.isValid()) {
            sendBuffer.clear();
            sendBuffer.put("Server message: 200 ok".getBytes());
            sendBuffer.flip();
            int count = socketChannel.write(sendBuffer);

            status = READ;
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

}