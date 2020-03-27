package com.mocrowu.cxd.builder;

public class MealB extends MealBuilder {
    @Override
    public void buildFood() {
        meal.setFood("一个汉堡~~~");
    }

    @Override
    public void buildDrink() {
        meal.setDrink("一杯啤酒~~~");
    }
}
