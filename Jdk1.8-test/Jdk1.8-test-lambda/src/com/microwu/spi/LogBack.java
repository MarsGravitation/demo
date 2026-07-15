package com.microwu.spi;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2025/3/25   13:52
 * Update History:
 * Author        Time            Content
 */
public class LogBack implements Log {
    @Override
    public void log(String info) {
        System.out.println("LogBack: " + info);
    }
}
