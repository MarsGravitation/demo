package com.microwu.cxd.design.abstract_factory;

/**
 * Description:     抽象工厂模式是围绕一个超级工厂创建其他工厂. 该超级工厂又称为其他工厂的工厂.
 *      这种类型的设计模式属于创建型模式.
 *      在抽象工厂模式中, 接口是负责创建一个相关对象的工厂, 不需要显示指定他们的类.
 *      每个生成的工厂都能按照工厂模式提供对象.
 *
 * 意图: 提供一个创建一系列相关或者相关依赖的接口, 而无需指定它们具体的类
 * 主要解决: 主要解决接口选择问题
 * 如何使用: 系统的产品有多于一个的产品族, 而系统只消费其中某一族的产品
 * 关键代码: 在一个工厂里聚合多个同类产品
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:20
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AbstractFactoryDemo {
    public static void main(String[] args) {
        // 获取形状工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");
        Shape circle = shapeFactory.getShape("CIRCLE");
        circle.draw();

        AbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");
        Color red = colorFactory.getColor("RED");
        red.fill();

    }
}