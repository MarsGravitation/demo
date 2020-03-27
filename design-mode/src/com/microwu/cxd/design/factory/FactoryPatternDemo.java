package com.microwu.cxd.design.factory;

/**
 * Description:    工厂模式属于创建型模式, 它提供了一种创建对象的最佳方式. 在工厂模式中, 我们在创建对象时不会对客户暴露创建逻辑,
 *                  而是通过使用一个共同的接口来指向新创建的对象
 *
 * 意图: 定义一个创建对象的接口, 让其子类自己决定实例化那个工厂类, 工厂模式使其创建过程延迟到子类进行
 * 解决: 主要解决接口选择的问题
 * 关键代码: 创建过程在其子类执行
 * 应用实例: 1. 需要一辆汽车, 直接从工厂里面提货
 *          2.hibernate换数据库只需要更换方言和驱动就可以
 * 使用场景: 1. 日志记录器: 记录可能记录到本地硬盘, 系统时间, 远程服务器等, 用户可以选择记录记录到什么地方
 *          2. 数据库访问: 当用户不知道最后系统采用那一类数据库, 以及数据库可能有变化时
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   13:49
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FactoryPatternDemo {
    public static void main(String[] args) {
        Shape circle = ShapeFactory.getShape("CIRCLE");
        circle.draw();

    }
}