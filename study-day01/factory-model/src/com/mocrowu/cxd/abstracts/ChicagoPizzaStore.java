package com.mocrowu.cxd.abstracts;

public class ChicagoPizzaStore extends PizzaStore{
    @Override
    Pizza createPizza(String type) {
        Pizza pizza = null;
        if(type == "c"){
            pizza = new ChicagoStyleCheesePizza();
        }
        return pizza;
    }
}
