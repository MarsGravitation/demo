package com.microwu.cxd.nio;

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
 * Description: https://www.cnblogs.com/snailclimb/p/9086334.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/19   15:24
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8080));
        // 设置成非阻塞模式
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        // 向Selector注册通道, 感兴趣的事件是accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        writeBuffer.put("received".getBytes());
        writeBuffer.flip();

        while(true) {
            int ready = selector.select();
            if(ready == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if(key.isAcceptable()) {
                    // 创建新连接, 并把连接注册到Selector上, 而且这个Channel只对读操作感兴趣
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    readBuffer.clear();
                    socketChannel.read(readBuffer);

                    readBuffer.flip();
                    System.out.println("received: " + new String(readBuffer.array()));
                    key.interestOps(SelectionKey.OP_WRITE);
                } else if(key.isWritable()) {
                    // rewind position = 0, 重读buffer的所有数据
                    writeBuffer.rewind();
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    socketChannel.write(writeBuffer);
                    key.interestOps(SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }

    }
}