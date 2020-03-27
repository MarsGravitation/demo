package com.mocrowu.cxd.abstract_factory;

import com.mocrowu.cxd.abstract_factory.pojo.NYPizzaStore;
import com.mocrowu.cxd.abstract_factory.pojo.Pizza;

public class AbstractFactoryTest {
    public static void main(String[] args) {
        System.out.println("----纽约芝士披萨-------");
        Pizza pizza = new NYPizzaStore().orderPizza("cheese");
        System.out.println("I order a " + pizza.getName());
    }
}
