package com.microwu.net.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/27   15:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SuppressWarnings("Duplicates")
public class BioClient {
    public static void main(String[] args) {
        Socket clientSocket = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            // 新建一个 Socket 请求
            clientSocket = new Socket("localhost", 8080);
            System.out.println("创建客户端连接成功。。。");
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请输入一个字符串。。。");
                String string = scanner.nextLine();
                if ("exit".equalsIgnoreCase(string)) {
                    System.out.println("客户端结束。。。");
                    break;
                }
                outputStream.write(string.getBytes());
                int length = 0;
                if ((length = inputStream.read(buffer)) != -1) {
                    System.out.println("服务端响应：" + new String(buffer, 0, length));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}