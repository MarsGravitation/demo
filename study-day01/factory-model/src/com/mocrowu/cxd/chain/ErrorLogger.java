package com.mocrowu.cxd.chain;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/4/25   17:13
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ErrorLogger extends AbstractLogger {
    public ErrorLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("Error Console::Logger: " + message);
    }
}