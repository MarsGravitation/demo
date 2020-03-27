package com.microwu.cxd.design.builder;

/**
 * Description:     建造者模式使用多个简单的对象一步一步构建成一个复杂的对象. 属于创建型模式
 *
 * 意图: 将一个复杂的构建与其表示相分离, 使得同样的构建过程可以构建不同的表示
 * 何时使用: 一些基本部件不会变, 而其组合经常变化的时.
 * 如何解决: 将变与不变分离开
 * 关键代码: 建造者, 创建和提供实例. 导演: 管理建造出来的实例的依赖关系
 * 使用场景: 1. 需要生成的对象具有复杂的内部结构
 *          2. 需要生成的对象内部属性本身相互依赖
 * 注意事项: 与工厂模式的区别是: 建造者模式更加关注与零件装配的顺序
 *
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:46
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BuilderPatternDemo {
    public static void main(String[] args) {
        MealBuilder mealBuilder = new MealBuilder();

        Meal meal = mealBuilder.prepareNonVegMeal();
        System.out.println("Veg Meal");
        meal.showItems();
        System.out.println("Total Cost: " + meal.getCost());

    }
}