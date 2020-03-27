package com.mocrowu.cxd.abstracts;

public class NYPizzaStore extends PizzaStore {
    @Override
    Pizza createPizza(String type) {
        Pizza pizza = null;
        if(type == "c"){
            pizza = new NYStyleChinesePizza();
        }
        return pizza;
    }
}
