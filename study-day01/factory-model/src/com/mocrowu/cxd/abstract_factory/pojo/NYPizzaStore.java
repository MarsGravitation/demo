package com.mocrowu.cxd.abstract_factory.pojo;

import com.mocrowu.cxd.abstract_factory.NYPizzaIngredientFactory;
import com.mocrowu.cxd.abstract_factory.PizzaIngredientFactory;

public class NYPizzaStore extends PizzaStore {
    @Override
    protected Pizza createPizza(String type) {
        Pizza pizza = null;
        PizzaIngredientFactory pizzaIngredientFactory = new NYPizzaIngredientFactory();
        if(type.equals("cheese")){
            pizza = new CheesePizza(pizzaIngredientFactory);
            pizza.setName("NY cheese pizza");
        }
        return pizza;
    }
}
