package com.mocrowu.cxd.template_method;

public abstract class CaffeineBeverage {
    /**
     * 模版方法：
     *  用于控制流程，声明为final，禁止子类更改操作流程
     */
    final void prepareRecipe(){
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }
    public void boilWater(){
        System.out.println("把水煮沸。。。。");
    }
    public abstract void brew();
    public void pourInCup(){
        System.out.println("倒入杯子。。。。");
    }
    public abstract void addCondiments();
}
