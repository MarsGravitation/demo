package com.microwu.cxd.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/25   11:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ZeroClient {
    public static void main(String[] args) throws IOException {
        // 创建sc
        SocketChannel sc = SocketChannel.open();
        // 绑定端口
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        // 设置为非阻塞模式
        sc.configureBlocking(false);

        while (!sc.finishConnect()) {

        }
        System.out.println("cxd 客户端连接成功 ...");

        // 创建Selector
        Selector selector = Selector.open();
        // 服务端注册 accept 事件
        sc.register(selector, SelectionKey.OP_WRITE);

        while (true) {
            // 阻塞, 返回值 - 有多少通道已经就绪
            int readyNum = selector.select();
            if (readyNum > 0) {
                continue;
            }

            // 获取所有的就绪事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 处理完需要把就绪事件手动删除
//                iterator.remove();
                if (key.isValid() && key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    // 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    String content = new String(bytes);
                    System.out.println("server send content: " + content);
                    buffer.clear();
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 写数据
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        String content = scanner.nextLine();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.put(content.getBytes());
                        buffer.flip();
                        socketChannel.write(buffer);
                        buffer.clear();
                        break;
                    }
                }
            }
        }


    }
}