package com.microwu.cxd.nio.upd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/21   10:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Upd {
    /**
     * 启动
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/20  14:35
     *
     * @param
     * @return  void
     */
    public void start() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress("10.0.0.53", 8080));
        channel.configureBlocking(false);

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);

        while(true) {
            int readyNum = selector.select();
            if(readyNum == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while(iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();

                if(next.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    DatagramChannel c = (DatagramChannel) next.channel();
                    c.receive(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    String headerString = new String(bytes);
                    System.out.println(headerString);

                    next.interestOps(SelectionKey.OP_READ);

                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new Upd().start();

    }
}