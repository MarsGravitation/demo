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
 * Description: https://blog.csdn.net/shanchahua123456/article/details/79546056
 * <p>
 * Selector 执行选择的过程:
 * 1. 首先检查已取消键集合, 也就是通过cancle() 取消的键. 如果该集合不为空, 则清空该集合里的
 * 键, 同时该集合中的每个取消的键也将从已注册集合和已选择键集合中移除(一个键被取消时,
 * 并不会立即充集合中移除, 而是将该键"拷贝"到已取消键集合中, 这种取消策略"延迟取消")
 * <p>
 * 2. 再次检查已注册键集合(准确的是该集合中的每个键的interest集合). 系统底层会依次询问每个
 * 已经注册的通道是否准备好选择器所感兴趣的某种操作, 一旦发现某个通道已经就绪了, 则
 * 会首先判断该通道是否存在已选择键集合中, 如果已经存在, 则更新该通道在已注册键集合
 * 中对应的ready集合, 如果不存在, 则首先清空该通道的对应的ready集合, 然后重设ready集合,
 * 最后将该键存至已注册键集合中. 这里需要明白, 当更新ready 集合时, 在上次select() 中
 * 就绪的操作不会被删除, 也就是ready集合中的元素时累计的 , 比如在第一次的Selector对某个
 * 通道的read 和write操作感兴趣, 在第一次执行select() 时,该通道的read操作就绪, 此时该
 * 通道对应的键中的ready集合存在read元素,在第二次执行select() 时, 该通道的write操作也就绪了,
 * 此时该通道对应的ready集中将同时有read 和write元素
 * <p>
 * 选择器不会主动删除被添加到已选择键集合中的键, 而且被添加到已选择键集合中的ready集合只能被设置,
 * 而不能被清理. 如果我们希望情况已选择键集合中的某个键中的ready集合该怎么办?
 * 我们知道一个键在新加入已选择键集合之前会首先置空某个键的ready集合. 被移除的键如果在下一次的select
 * 中再次就绪, 它将会被重新加入已选择的键的集合中. 这就是为什么每次调用remove
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/25   11:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ZeroServer {
    public static void main(String[] args) throws IOException {
        // 创建ssc
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 绑定端口
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8080));
        // 设置为非阻塞模式
        ssc.configureBlocking(false);
        System.out.println("cxd 服务器启动, 正在监听8080 ...");

        // 创建Selector
        Selector selector = Selector.open();
        // 服务端注册 accept 事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

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
                iterator.remove();
                if (key.isValid() && key.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
//                    iterator.remove();
                } else if (key.isValid() && key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    // 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    String content = new String(bytes);
                    System.out.println("client send content: " + content);
                    buffer.clear();
                    // 注册感兴趣的事件, 因为每次遍历都把感兴趣的事件删除
                    // 所以需要注册下一次感兴趣的事件
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    // 写数据
                    String content = "欢迎来到cxd 的世界";
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put(content.getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);
                    buffer.clear();
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
            }
        }


    }
}