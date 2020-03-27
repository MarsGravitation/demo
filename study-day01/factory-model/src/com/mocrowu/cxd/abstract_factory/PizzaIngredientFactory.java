package com.mocrowu.cxd.abstract_factory;

import com.mocrowu.cxd.abstract_factory.pojo.*;

public interface PizzaIngredientFactory {
    public Dough createDough(); // 面团
    public Sauce createSauce(); // 调味汁
    public Cheese createCheese(); // 奶酪
    public Veggies[] createViggers(); // 蔬菜
    public Pepperoni createPepperoni(); // 意大利香肠
    public Clams createClams(); // 蚌
}
