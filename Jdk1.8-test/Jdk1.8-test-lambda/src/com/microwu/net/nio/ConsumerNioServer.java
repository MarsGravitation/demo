package com.microwu.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/6   13:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConsumerNioServer implements Runnable {

    private ServerSocketChannel ssc;

    private Selector selector;

    private int port;

    private boolean stop;

    public ConsumerNioServer(int port) {
        this.port = port;
        try {
            // 1. 创建服务端 Channel
            ssc = ServerSocketChannel.open();
            // 2. 创建选择器
            selector = Selector.open();
            // 3. 设置非阻塞模式
            ssc.configureBlocking(false);
            // 4. 绑定一个端口和等待队列
            ssc.bind(new InetSocketAddress(port), 1024);
            // 5. 注册到选择器，关注接受事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                // 阻塞到有客户端连接
                int select = selector.select();
                System.out.println(select);
                // 获取就绪事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    handle(selectionKey);
                    iterator.remove();
                }

            }

            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理感兴趣事件
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/6  14:07
     *
     * @param   	selectionKey
     * @return  void
     */
    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            if (selectionKey.isAcceptable()) {
                // 可接受事件
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                // 三次握手完成，建立物理连接
                SocketChannel channel = serverSocketChannel.accept();
                // 设置非阻塞模式
                channel.configureBlocking(false);
                // 注册可读事件
                channel.register(selector, SelectionKey.OP_READ);
            } else if (selectionKey.isReadable()) {
                // 可读事件
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                // 读取到 buffer 中
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = socketChannel.read(buffer);
                if (read > 0) {
                    // 转到字节数组中
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    System.out.println("客户端消息：" + new String(bytes));
                } else {
                    // 释放资源
                    selectionKey.channel();
                    socketChannel.close();
                }
                // 注册一个可写事件
//                socketChannel.register(selector, SelectionKey.OP_WRITE);
                write(socketChannel);
            } else if (selectionKey.isWritable()) {
                // 可读事件
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                // 可写事件
                byte[] bytes = "success".getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                // 写入 Channel
                socketChannel.write(buffer);
                // 关注可读事件
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
        }

    }

    private void write(SocketChannel socketChannel) throws IOException {
        // 可写事件
        byte[] bytes = "success".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        // 写入 Channel
        socketChannel.write(buffer);
    }

    public static void main(String[] args) {
        new Thread(new ConsumerNioServer(8080)).start();
    }
}