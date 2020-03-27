package com.mocrowu.cxd.builder;

public class MealA extends MealBuilder {
    @Override
    public void buildFood() {
        meal.setFood("一份面包~~~");
    }

    @Override
    public void buildDrink() {
        meal.setDrink("一杯果汁~~~");
    }
}
