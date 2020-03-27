package com.mocrowu.cxd.builder;

public abstract class MealBuilder {
    Meal meal = new Meal();

    public Meal getMeal(){
        return meal;
    }

    public abstract void buildFood();
    public abstract void buildDrink();
}
