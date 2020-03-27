package com.mocrowu.cxd.builder;

/**
 * 建造者的角色：
 *      1.Product：产品角色 --- 套餐(Meal)
 *      2.Builder：抽象建造者 --- MealBuilder；创建产品各个部件的指定抽象接口
 *      3.ConcreteBuilder：具体建造者 --- MealA，具体构建每个部件
 *      4.Director：指挥者 --- KFCWaiter，使用构建者创建产品
 */
public class Client {
    public static void main(String[] args) {
        KFCWaiter kfcWaiter = new KFCWaiter();
        MealA a = new MealA();
        kfcWaiter.setMealBuilder(a);
        Meal mealA = kfcWaiter.construct();
        System.out.println(mealA.getFood()+"---"+mealA.getDrink());
    }
}
