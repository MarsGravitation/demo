package com.mocrowu.cxd.abstract_factory.pojo;

import com.mocrowu.cxd.abstract_factory.PizzaIngredientFactory;

public class ClamsPizza extends Pizza {
    private PizzaIngredientFactory pizzaIngredientFactory;

    public ClamsPizza(PizzaIngredientFactory pizzaIngredientFactory) {
        this.pizzaIngredientFactory = pizzaIngredientFactory;
    }

    @Override
    protected void prepare() {
        System.out.println("prepare     " + name);
        dough = pizzaIngredientFactory.createDough();
        sauce = pizzaIngredientFactory.createSauce();
        cheese = pizzaIngredientFactory.createCheese();
        clams = pizzaIngredientFactory.createClams();
    }
}
