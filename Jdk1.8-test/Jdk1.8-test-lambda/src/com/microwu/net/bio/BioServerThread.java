package com.microwu.net.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description: 引入多线程，解决了只能接受一个客户端的问题
 *  弊端：因为线程和客户端是 1:1 的关系，如果客户端请求量增大，线程数随着上升，会极大地消耗 cpu 资源
 *
 *  引入线程池后，避免创建大量线程创建新的 Socket， 多余线程池容量的请求只能阻塞
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/27   16:24
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BioServerThread {
    public static void main(String[] args) {
        int port = 8080;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            Socket socket = null;

            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
            while (true) {
                socket = serverSocket.accept();
                // 连接量大的时候 会拖垮 CPU
//                new Thread(new SocketHandler(socket)).start();
                // 线程池改进
                threadPoolExecutor.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}