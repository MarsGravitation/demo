package com.microwu.net.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Description:
 *  1. accept 阻塞，三次握手
 *  2. 然后 read 阻塞，等待客户端发送消息
 *  3. 客户端发送消息，完成了一次 socket 的连接和数据传输，但是 socket 并未关闭， 此时阻塞在 read 方法
 *  4. 如果关闭客户端，此次 Socket 通信结束，然后重新阻塞在 Accept 继续监听端口，等待新的客户端连接
 *
 * 弊端：
 *  如果已经存在一个 Socket 请求时，第二个 Socket 未得到结果，因为服务端线程阻塞在 read
 *  想要同时处理多个通信，必须建立多个 Socket
 *
 *
 *  https://www.cnblogs.com/jing99/p/11993041.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/27   15:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SuppressWarnings("Duplicates")
public class BioServerSingle {
    public static void main(String[] args) {
        // 服务端开启一个端口进行监听
        int port = 8080;
        // 服务端
        ServerSocket serverSocket = null;
        // 客户端
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            // 通过构造函数创建 ServerSocket ，指定监听端口
            // 如果端口合法且空闲，服务器就会监听成功
            serverSocket = new ServerSocket(port);
            // 通过无限循环监听客户端连接，如果没有客户端接入，就会阻塞在 accept 操作
            while (true) {
                System.out.println("等待一个新的连接。。。");
                socket = serverSocket.accept();
                in = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                // 阻塞
                while ((length = in.read(buffer)) > 0) {
                    System.out.println("接受到客户端消息：" + new String(buffer, 0, length));
                    out = socket.getOutputStream();
                    out.write("success".getBytes());
                    System.out.println("响应结束。。。");
                }
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 必要的清理活动
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}