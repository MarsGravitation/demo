package com.microwu.net.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/27   16:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SuppressWarnings("Duplicates")
public class SocketHandler implements Runnable {

    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                System.out.println("客户端消息：" + new String(buffer, 0, length));
                out.write("success".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
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