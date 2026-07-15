package com.cxd.hbase.utils;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/5   15:39
 * Update History:
 * Author        Time            Content
 */
public class HBaseConnect {

    // 设置静态属性 hbase 连接
    public static Connection connection = null;

    static {
        // 创建 hbase 的连接
        try {
            // 使用配置文件的方法
            connection = ConnectionFactory.createConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接关闭方法，用于进程关闭时调用
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2024/2/5 15:42
     *
     * @param:
     * @return: void
     */
    public static void closeConnection() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

}
