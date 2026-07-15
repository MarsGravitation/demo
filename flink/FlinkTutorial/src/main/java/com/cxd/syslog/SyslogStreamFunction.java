package com.cxd.syslog;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.apache.flink.util.NetUtils.isValidClientPort;
import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/4   15:46
 * Update History:
 * Author        Time            Content
 */
public class SyslogStreamFunction implements SourceFunction<String> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(SyslogStreamFunction.class);

    private final String hostname;
    private final int port;
    private final int bufferSize;

    private transient DatagramSocket currentDs;

    private volatile boolean isRunning = true;

    public SyslogStreamFunction(String hostname, int port, int bufferSize) {
        checkArgument(isValidClientPort(port), "port is out of range");
        checkArgument(bufferSize > 0, "bufferSize must be positive");
        this.hostname = checkNotNull(hostname, "hostname must not be null");;
        this.port = port;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {

        byte[] receiveData = new byte[bufferSize];
        DatagramPacket dp = null;
        while (isRunning) {
            try (DatagramSocket datagramSocket = new DatagramSocket(port, InetAddress.getByName(hostname))) {
                currentDs = datagramSocket;

                while (isRunning) {
                    dp = new DatagramPacket(receiveData, receiveData.length);

                    this.currentDs.receive(dp);

                    if (dp.getLength() > 0) {
                        String record = new String(receiveData, 0, dp.getLength());
                        ctx.collect(record);
                    }
                }
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;

        // we need to close the socket as well, because the Thread.interrupt() function will
        // not wake the thread in the socketStream.read() method when blocked.
        DatagramSocket theDs = this.currentDs;
        if (theDs != null) {
            theDs.close();
        }
    }
}
