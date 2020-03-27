package com.microwu.cxd.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/22   10:37
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TcpClient {
    //    public static Selector selector;
//    public static SocketChannel clntChan;
//
//    static {
//        try {
//            selector = Selector.open();
//            clntChan = SocketChannel.open();
//            clntChan.configureBlocking(false);
//            clntChan.connect(new InetSocketAddress("localhost", 8080));
//            clntChan.register(selector, SelectionKey.OP_READ);
//            while (!clntChan.finishConnect()){
//
//            }
//            System.out.println("已连接！");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        SocketChannel socketChannel = TcpClient.clntChan;
//        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
////        new Avca(selector,socketChannel).start();
//        while (true){
//            Scanner scanner = new Scanner(System.in);
//            String word = scanner.nextLine();
//            byteBuffer.put(word.getBytes());
//            byteBuffer.flip();
//            socketChannel.write(byteBuffer);
//            byteBuffer.clear();
//        }
//    }
//
//    static class Avca extends Thread{
//        private Selector selector;
//        private SocketChannel clntChan;
//
//        public Avca(Selector selector,SocketChannel clntChan){
//            this.selector = selector;
//            this.clntChan = clntChan;
//        }
//
//        @Override
//        public void run(){
//            try {
//                while (true){
//                    selector.select();
//                    Set<SelectionKey> keys = selector.selectedKeys();
//                    Iterator<SelectionKey> keyIterator = keys.iterator();
//                    ByteBuffer byteBuffer = ByteBuffer.allocate(256);
//                    while (keyIterator.hasNext()){
//                        SelectionKey selectionKey = keyIterator.next();
//                        if (selectionKey.isValid()){
//                            if (selectionKey.isReadable()){
//                                SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
//                                socketChannel.read(byteBuffer);
//                                byteBuffer.flip();
//                                byte[] bytes = new byte[byteBuffer.remaining()];
//                                byteBuffer.get(bytes);
//                                System.out.println(new String(bytes));
//                                byteBuffer.clear();
//                            }
//                        }
//                    }
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
    public static void main(String[] args) throws IOException {
        // 创建客户端通道
        SocketChannel sc = SocketChannel.open();
        // 设置非阻塞
        sc.configureBlocking(false);
        // 连接服务端
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));

        while(!sc.finishConnect()) {

        }
        System.out.println("客户端完成连接...");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 发送消息
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            buffer.put(line.getBytes());
            buffer.flip();
            sc.write(buffer);
            buffer.clear();

        }

    }
}