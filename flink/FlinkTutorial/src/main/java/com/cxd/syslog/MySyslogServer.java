package com.cxd.syslog;

import org.graylog2.syslog4j.SyslogConstants;
import org.graylog2.syslog4j.server.*;

import java.net.SocketAddress;

/**
 * Description: <a href="https://blog.csdn.net/chanllenge/article/details/105168697">...</a>
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/4   15:09
 * Update History:
 * Author        Time            Content
 */
public class MySyslogServer {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 32376;

    private void receiveSyslogMessage() throws InterruptedException {
        SyslogServerIF server = SyslogServer.getInstance(SyslogConstants.UDP);
        SyslogServerConfigIF config = server.getConfig();
        config.setHost(HOST);
        config.setPort(PORT);
        config.addEventHandler(new SyslogServerSessionEventHandlerIF() {
            @Override
            public Object sessionOpened(SyslogServerIF syslogServerIF, SocketAddress socketAddress) {
                return null;
            }

            @Override
            public void event(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, SyslogServerEventIF syslogServerEventIF) {
                System.out.println("receive from:" + socketAddress + "\tmessage" + syslogServerEventIF.getMessage());
            }

            @Override
            public void exception(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, Exception e) {

            }

            @Override
            public void sessionClosed(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, boolean b) {

            }

            @Override
            public void initialize(SyslogServerIF syslogServerIF) {

            }

            @Override
            public void destroy(SyslogServerIF syslogServerIF) {

            }
        });
        SyslogServer.getThreadedInstance(SyslogConstants.UDP);
        Thread.sleep(100000);
    }

    public static void main(String[] args) throws InterruptedException {
        new MySyslogServer().receiveSyslogMessage();
    }

}
