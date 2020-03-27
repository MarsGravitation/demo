package com.microwu.cxd.builder;

/**
 * Description:     Builder类
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   16:58
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MealBuilder {
    public Meal prepareVeMeal(){
        Meal meal = new Meal();
        meal.addItem(new VegBurger());
        meal.addItem(new Coke());
        return meal;
    }
}