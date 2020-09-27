package com.microwu.net.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   9:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AsyncHandler implements Runnable {

    private final Selector selector;

    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(2048);

    /**
     * 读取就绪
     */
    private final static int READ = 0;
    /**
     * 响应就绪
     */
    private final static int SEND = 1;
    /**
     * 处理中
     */
    private final static int PROCESSING = 2;

    private int status = READ;

    /**
     * 开启线程数为 5 的异步处理线程池
     */
    private static final ExecutorService workers = Executors.newFixedThreadPool(5);

    public AsyncHandler(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        this.selector = selector;
        this.selector.wakeup();
    }

    @Override
    public void run() {
        // 如果一个任务正在异步处理，那么这个 run 是直接不触发任何处理的， read 和 send 只负责简单的数据读取和响应
        // 业务处理完全不阻塞这里的处理
        switch (status) {
            case READ:
                read();
                break;
            case SEND:
                send();
                break;
                default:
        }

    }

    private void read() {
        if (selectionKey.isValid()) {
            try {
                readBuffer.clear();
                int read = socketChannel.read(readBuffer);
                if (read > 0) {
                    status = PROCESSING;
                    workers.execute(this::readWorker);
                }
            } catch (IOException e) {
                selectionKey.cancel();
                try {
                    socketChannel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void send() {
        if (selectionKey.isValid()) {
            status = PROCESSING;
            workers.execute(this::sendWorker);
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private void readWorker() {
        System.out.println("收到来自客户端的消息：" + new String(readBuffer.array()));

        status = SEND;

        // 唤醒阻塞在 select 的线程
        // 因为该 interestOps 写事件是放在子线程的，
        // select 在该 Channel 还是对 read 感兴趣时又被调用
        // 因此如果不主动唤醒，select 可能不会立刻 select 该读就绪事件
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        this.selector.wakeup();
    }

    private void sendWorker() {
        try {
            sendBuffer.clear();
            sendBuffer.put("Server Message：200 OK".getBytes());
            sendBuffer.flip();

            int write = socketChannel.write(sendBuffer);
            if (write < 0) {
                selectionKey.cancel();
                socketChannel.close();
            } else {
                status = READ;
            }
        } catch (IOException e) {
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}