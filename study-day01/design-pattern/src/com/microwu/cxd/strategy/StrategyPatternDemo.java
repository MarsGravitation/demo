package com.microwu.cxd.strategy;

/**
 * Description:    策略模式与状态模式的比较
 *          状态模式的类图和策略模式类似, 并且都是能够动态改变对象的行为.
 *          但是状态模式是通过状态转移来改变Context所组合的State对象, 而策略模式
 *          是通过Context本身的决策来改变组合的Strategy对象. 所谓的状态转移, 是指
 *          Context在运行过程中由于一些条件发生改变而使得State对象发生改变,
 *          注意必须是在运行过程中.
 *          状态模式主要是用来解决状态转移问题, 当状态发生了转移, 那么Context对象就会改变它的行为
 *          而策略模式主要是用来封装一些可以互相替换的算法族, 并且可以更具需要动态地去替换Context的算法
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/27   17:26
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StrategyPatternDemo {
    public static void main(String[] args) {
        Context context = new Context(new OperationAdd());
        int i = context.executeStrategy(1, 2);
        System.out.printf("执行加法策略, 结果 = " + i);
    }
}