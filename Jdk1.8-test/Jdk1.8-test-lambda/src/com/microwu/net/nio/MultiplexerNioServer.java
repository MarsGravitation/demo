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
 *  创建 NIO 服务端的主要步骤：
 *      1. 打开 ServerSocketChannel，监听客户端连接
 *      2. 绑定端口，设置连接为非阻塞模式
 *      3. 创建 Reactor 线程，创建多路复用器并启动线程
 *      4. 将 ServerSocketChannel 注册到 Reactor 线程中的 Selector 上，监听 ACCEPT 事件
 *      5. Selector 轮询准备就绪的 key
 *      6. Selector 监听到新的客户端接入，处理新的接入请求，完成 TCP 三次握手，建立物理连接
 *      7. 设置客户端连接为非阻塞模式
 *      8. 将新接入的客户端连接注册到 Reactor 线程的 Selector 上，监听读操作，读取客户端发送的网络消息
 *      9. 异步读取客户端消息到缓冲区
 *      10. 对 buffer 编解码，处理半包消息，将解码成功的消息封装成 Task
 *      11. 将应答消息编码为 buffer，调用 SocketChannel 的 write 将消息异步发送给客户端
 *
 * NIO 注册、轮询等待、读写操作协作关系：
 *  1. 向 Selector 注册连接、读、写IO 事件
 *  2. 轮询 Selector 中的 SelectKey 集合
 *  3. 通过 key 找到对应的 Channel，通过绑定的 buffer 进行读写
 *
 * Selector：
 *  Selector 维护一个 已经注册的 Channel 容器，应用程序将向 Selector 对象注册它需要关注的 Channel，一局具体的某一个 Channel 会对 IO 事件感兴趣
 * Channel：
 *
 *
 * https://www.cnblogs.com/jing99/p/12000371.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/27   16:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MultiplexerNioServer implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean stop = false;

    public MultiplexerNioServer(int port) {
        try {
            // 获得一个 serverChannel
            serverSocketChannel = ServerSocketChannel.open();
            // 创建选择器
            selector = Selector.open();
            // 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 绑定一个端口和等待队列长度
            serverSocketChannel.bind(new InetSocketAddress(port), 1024);
            // 注册 Channel 到 Selector，关注连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stop = false;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                // 无论是否有读写事件，selector 每隔 1s 被唤醒一次。如果一定时间内没有事件，就需要做些其他的事件，就可以使用带超时的
//                int client = selector.select(1000);
//                System.out.println(client);
                // 阻塞，只有至少有一个注册的事件发生的时候才会继续
                // 不设置超时时间为线程阻塞，但是 IO 上支持多个文件描述符就绪
                int client = selector.select();
//                System.out.println(client);
//                if (client == 0) {
//                    continue;
//                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    try {
                        // 处理事件
                        handle(key);
                        iterator.remove();
                    } catch (Exception e) {
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
            } finally {

            }

        }

        if (selector != null) {
            // selector 关闭后会自动释放里面管理的资源
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 连接事件
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 通过 ssc 的 accept 创建 SocketChannel 实例
                // 完成该操作意味着 TCP 三次握手，TCP 物理链路正式建立
                SocketChannel sc = ssc.accept();
                System.out.println("和客户端建立连接。。。");
                sc.configureBlocking(false);
                // 连接建立后，关注读事件
                sc.register(selector, SelectionKey.OP_READ);
            }

            // 读事件
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 读取请求码流，返回读取到的字节数
                int readBytes = socketChannel.read(buffer);
                // 读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    // 将缓冲区当前 limit 设置为 position = 0，用于后续对缓冲区的读取操作
                    // 读写模式反转
                    buffer.flip();
                    // 将缓冲区可读字节复制到新建的数组中
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body = new String(bytes);
                    System.out.println("客户端消息：" + body);

                    res(socketChannel, body);
                } else if (readBytes < 0) {
                    // 链路已关闭，释放资源
                    key.cancel();
                    socketChannel.close();
                } else {
                    // 没有读到字节 忽略
                }
            }
        }
    }

    private void res(SocketChannel socketChannel, String body) throws IOException {
        if (body != null && body.length() > 0) {
            byte[] bytes = body.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        }

    }

    public static void main(String[] args) {
        int port = 8080;
        MultiplexerNioServer nioServer = new MultiplexerNioServer(port);
        new Thread(nioServer, "nio-server-001").start();
    }
}