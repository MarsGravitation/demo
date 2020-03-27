package com.microwu.cxd.network.netty.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/27   14:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class UdpClient {
    public static void main(String[] args) throws Exception {
        String str = "北京小悟科技有限公司http://www.microwu.com";
        sendUDP(str.getBytes());
    }


    public static void sendUDP(byte[] message) {
        String host = "127.0.0.1";
        int port = 8080;

        try {
            // 根据主机名称得到IP地址
            InetAddress address = InetAddress.getByName(host);
            // 用数据和地址创建数据报文包
            DatagramPacket packet = new DatagramPacket(message, message.length, address, port);

            // 创建数据报文套接字并通过它传送
            DatagramSocket dsocket = new DatagramSocket();
            dsocket.send(packet);
            System.out.println("发送成功！！！！");

            dsocket.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}