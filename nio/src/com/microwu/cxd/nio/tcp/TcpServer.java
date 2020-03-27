package com.microwu.cxd.nio.tcp;

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
 * Date:       2019/11/22   10:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TcpServer {
    public static void main(String[] args) throws IOException {
        // 创建服务端Channel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 绑定端口
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8080));
        // 设置非阻塞模式
        ssc.configureBlocking(false);
        System.out.println("cxd 服务器开启 ...");

        // 创建selector
        Selector selector = Selector.open();
        // 注册 accept 事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while(true) {
            // 查询就绪事件
            int readyNum = selector.select();
            if(readyNum == 0) {
                continue;
            }

            // 有事件就绪
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel c = channel.accept();
                    c.configureBlocking(false);
                    c.register(selector, SelectionKey.OP_READ);
                } else if(key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.clear();
                    channel.read(buffer);

                    // 转换读模式
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    System.out.println(new String(bytes));

                    channel.register(selector, SelectionKey.OP_READ);

                }
            }

        }

    }
}