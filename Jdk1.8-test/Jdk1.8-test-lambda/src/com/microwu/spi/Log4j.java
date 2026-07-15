package com.microwu.spi;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2025/3/25   13:51
 * Update History:
 * Author        Time            Content
 */
public class Log4j implements Log {
    @Override
    public void log(String info) {
        System.out.println("Log4j: " + info);
    }
}
