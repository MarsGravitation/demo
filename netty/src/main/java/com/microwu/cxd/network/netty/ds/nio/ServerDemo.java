package com.microwu.cxd.network.netty.ds.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * 对 JDK NIO demo 的抽象和封转，并解决了一些 bug 的过程
 *
 * select 方法
 *  例如，有两个已经建立的 Channel，分别是 A 和 B，而且 A 和 B 分别注册到了一个 Selector 上，接着在该 Selector
 *  调用 select()：
 *      - 第一次调用 select，发现只有 A 有 I/O 事件就绪，select 会立即返回 1，然后处理之
 *      - 第二次调用 select，防线另一个通道 B 也有 I/O 事件就绪，此时 select 还是返回 1，即是自上次 select 后开始计算的
 *      注意：如果第一次轮训后，对 A 没有做任何操作，那么就有两个就绪的 Channel
 *
 *  select 返回后可通过其返回值判断有没有 Channel 就绪，如果有就绪的 Channel，那么就可以使用 selectedKeys 方法拿到就绪的
 *  Channel 及其一些属性
 *
 *  Set<SelectionKey> selectedKeys = selector.selectedKeys();
 *
 *  当给 Selector 注册 Channel 时，调用的 register 方法会返回 SelectionKey 对象，这个对象代表了注册到
 *  该 Selector 的 Channel，可以遍历这个集合来访问就绪的通道
 *
 *  ServerSocketChannel ss = (ServerSocketChannel) selectionKey.channel();
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484542&idx=1&sn=bfa8c7bf2dfb6c3edf19f6598a13ee77&chksm=fbc0927eccb71b68113a40c5b0d2ef955508ccc598465bb30daab6a70e56470af4f500250011&scene=21#wechat_redirect
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/9  19:18
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class ServerDemo {

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverSocketChannel = SelectorProvider.provider().openServerSocketChannel();
        serverSocketChannel.bind(new InetSocketAddress(8000));
        final Selector selector = SelectorProvider.provider().openSelector();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select() <= 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel ss = (ServerSocketChannel) selectionKey.channel();
                    // 虽然 accept 是阻塞式 API，但是此时一定是不会阻塞的，立即完成 TCP 三次握手，建立逻辑连接
                    SocketChannel socketChannel = ss.accept();
                    // 这里应该额外启动 I/O 线程下面的逻辑，selector 最好也分开，新建一个 selector 专门轮训已建连接的 I/O 事件
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }

}
