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
 * Date:       2020/8/4   10:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NioClientHandler implements Runnable {

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            // 创建选择器
            selector =  Selector.open();
            // 打开监听通道
            socketChannel = SocketChannel.open();
            // 开启非阻塞模式
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            try {
                int wait = selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handle(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite(sc);
                } else {
                    System.exit(1);
                }
            }
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("res: " + body);
                    this.stop = true;
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            System.out.println("客户端和服务端建立连接。。。");
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        // 将消息编码为字节数组
        byte[] request = "0123456789".getBytes();
        // 根据数组容量创建 ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
        // 将字节数组复制到缓冲区
        writeBuffer.put(request);
        // flip 切换读写操作
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("客户端写入完成");
        }
    }

    public static void main(String[] args) {
        new Thread(new NioClientHandler("localhost", 8080), "nioClient-001").start();
    }
}