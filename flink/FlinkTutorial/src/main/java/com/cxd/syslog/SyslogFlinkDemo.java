package com.cxd.syslog;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/4   15:42
 * Update History:
 * Author        Time            Content
 */
public class SyslogFlinkDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new SyslogStreamFunction("0.0.0.0", 514, 1024))
                .name("Syslog Stream")
                .print();

        env.execute();
    }

}
