package com.mocrowu.cxd.abstracts;

/**
 *  一、问题：
 *      上一个案例通过简单工厂模式或者静态工厂模式实现了创建对象和使用对象的分离，可以根据type生产对应的口味披萨
 *  现在，我需要根据不同地域生产不同口味的披萨，如果使用简单工厂模式，我们需要两个工厂NY、Chicago工厂，该地域有
 *  很多披萨店，它们并不希望按照总店的流程来制作披萨，它们希望用自己的流程；但是简单工厂模式是将披萨的制作流程全部承包
 *  二、解决方案
 *      我们可以将披萨的制作交给披萨店（披萨分工厂）做，但是订单要交给披萨总店(总工厂）做，披萨分店
 *  可以根据口味生产不同口味的披萨，而我们可以选择不同地域的披萨分店
 *  三、优缺点
 *      相比简单工厂模式，当新增地域时，不需要更改原工厂，只需要增加对应的工厂
 *
 */
public class PizzaTestDrive {
    public static void main(String[] args) {
        System.out.println("---------成旭东需要一份NY的面包---------");
        Pizza dPizza = new NYPizzaStore().createPizza("c");
        System.out.println("成旭东 order a " + dPizza.getName());
        System.out.println("---------成旭军需要一份Chicago的披萨--------");
        Pizza jPizza = new ChicagoPizzaStore().createPizza("c");
        System.out.println("成旭军 order a " + jPizza.getName());

    }
}
