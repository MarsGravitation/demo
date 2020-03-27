package com.mocrowu.cxd.abstracts;

public class ChicagoStyleCheesePizza extends Pizza{
    public ChicagoStyleCheesePizza(){
         name = "Chicago Style Deep Dish Cheese Pizza";
         dough = "Extra Thick Crust Dough";
         sause = "Plum Tomato Sauce";

         topping.add("Shredded Mozzarella Cheese");
    }

    public void cut(){
         System.out.println("Cutting the Pizza into square slices");
    }
}
