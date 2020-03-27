package com.mocrowu.cxd.chain;

/**
 * Description:     责任链,使用场景:
 *          1.有多个对象可以处理同一个请求时，具体那个对象该请求由运行时刻自动确定
 *          2.在不明确指定接受者的情况下，向多个对象中的一个提交一个请求
 *          3.可动态指定一组对象处理请求
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/4/25   17:15
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ChainPatternDemo {

    public static AbstractLogger getChainOfLoggers() {
        ErrorLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
        FileLogger fileLogger = new FileLogger(AbstractLogger.DEBUG);
        ConsoleLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);

        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(consoleLogger);
        return errorLogger;
    }

    public static void main(String[] args) {
        AbstractLogger chainOfLoggers = getChainOfLoggers();

        chainOfLoggers.logMessage(AbstractLogger.INFO, "This is an information");
        chainOfLoggers.logMessage(AbstractLogger.DEBUG, "This is a debug level information");
        chainOfLoggers.logMessage(AbstractLogger.ERROR, "This is an error information");

    }
}