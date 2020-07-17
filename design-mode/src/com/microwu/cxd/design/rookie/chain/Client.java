package com.microwu.cxd.design.rookie.chain;

/**
 * Description:     抽象处理者角色, 定义一个处理请求的接口, 定义下一个处理者
 *                  具体处理角色, 具体处理者接到请求后, 可以选择自己处理, 也可以交给下家处理
 *
 * 使用场景:
 *  有多个对象可以处理同一个请求, 具体那个对象处理该请求由运行时刻自动确定
 *  在不确定指定接受者的情况下, 向多个对象中的某一个对象提交一个请求
 *  可动态指定一组对象的处理请求
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/20   16:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Client {
    public static void main(String[] args) {
        ConcreteHandlerOne one = new ConcreteHandlerOne();
        ConcreteHandlerTwo two = new ConcreteHandlerTwo();
        // 初始化责任链
        one.setNextHandler(two);
        one.handleMessage(1);
        one.handleMessage(2);
        one.handleMessage(4);

    }
}