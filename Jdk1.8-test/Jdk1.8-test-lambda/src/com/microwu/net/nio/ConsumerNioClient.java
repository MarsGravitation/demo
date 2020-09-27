package com.microwu.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/6   14:37
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConsumerNioClient implements Runnable {

    private String host;

    private int port;

    private SocketChannel socketChannel;

    private Selector selector;

    public ConsumerNioClient(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            // 1. 创建客户端 Channel
            socketChannel = SocketChannel.open();
            // 2. 设置为非阻塞模式
            socketChannel.configureBlocking(false);
            // 3. 创建选择器
            selector = Selector.open();
            // 4. 连接服务器
            if (socketChannel.connect(new InetSocketAddress(host, port))) {

            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
            // 5. 注册可写事件
//            socketChannel.register(selector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 阻塞直到有事件来到
                int select = selector.select();
                System.out.println(select);
                // 获取感兴趣事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    if (selectionKey.isConnectable()) {
                        // 连接事件
                        if (socketChannel.finishConnect()) {
//                            byte[] bytes = "Hello, Server!!!".getBytes();
//                            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
//                            // 写入 buffer
//                            buffer.put(bytes);
//                            buffer.flip();
//                            socketChannel.write(buffer);
//                            // 注册可读事件
//                            socketChannel.register(selector, SelectionKey.OP_READ);
                              socketChannel.register(selector, SelectionKey.OP_WRITE);
                        }
                    } else if (selectionKey.isWritable()) {
                        // 可写事件
                        byte[] bytes = "Hello, Server!!!".getBytes();
                        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                        // 写入 buffer
                        buffer.put(bytes);
                        buffer.flip();
                        socketChannel.write(buffer);
                        // 注册可读事件
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        // 可读事件
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        System.out.println("服务端消息：" + new String(bytes));
                        return;

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ConsumerNioClient("localhost", 8080)).start();
    }
}