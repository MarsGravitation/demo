package com.microwu.cxd.abstract_factory;

/**
 * Description:
 *  简单工厂模式：
 *      简单工厂模式不是23中的一种，简而言之买就是有一个专门生产某个产品的类
 *      案例：
 *          比如鼠标工厂，专门生产鼠标，0 - 戴尔，1 - 惠普
 *          鼠标接口、戴尔鼠标实现类、惠普实现类、鼠标简单工厂
 *      描述：
 *          专门定义一个类来负责创建其他类的实例，被创建的实例通常具有相同的父类
 *  工厂模式：
 *      工厂模式也就是鼠标工厂是个父类，有生产鼠标的接口，戴尔工厂和惠普工厂继承它，
 *      生产鼠标不由参数决定，而是创建鼠标工厂的时候，由工厂创建，后续调用工厂创建鼠标即可
 *      案例：
 *          鼠标工厂接口、惠普工厂实现类、戴尔工厂实现类
 *          鼠标接口、惠普鼠标、戴尔鼠标
 *      描述： 定义一个用户创建对象的接口，让子类决定实例化哪个类，使一个雷的实例化延迟到子类
 *  抽象工厂模式：
 *      抽象工厂模式不仅生产鼠标，还生产键盘，PC厂商是个接口，有生产鼠标和键盘的接口
 *      惠普工厂和戴尔工厂实现，分别生产各自的产品
 *      案例：
 *          PC接口、惠普工厂、戴尔工厂
 *          鼠标接口、惠普鼠标、戴尔鼠标
 *          键盘接口、惠普键盘、戴尔键盘
 *      描述：
 *          提供一个创建一系列相关或者相互依赖对象的接口，而无需指定它们具体的类
 *      当产品有一个的时候，抽象工厂模式及变成工厂模式；当工厂模式的产品变成多个时，就变成了抽象工厂模式
 *
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   14:32
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AbstractFactoryPatternDemo {
    public static void main(String[] args) {
        //  获取形状工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory("shape");
        // 获取形状
        Shape circle = shapeFactory.getShape("circle");
        circle.draw();
        // 获取颜色工厂
        AbstractFactory colorFactory = FactoryProducer.getFactory("color");
        // 获取颜色
        Color red = colorFactory.getColor("red");
        red.fill();

    }
}