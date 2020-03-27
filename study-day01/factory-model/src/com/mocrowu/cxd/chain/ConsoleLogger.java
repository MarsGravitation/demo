package com.mocrowu.cxd.chain;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/4/25   17:12
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConsoleLogger extends AbstractLogger {
    public ConsoleLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Standard Console::Logger: " + message);
    }
}