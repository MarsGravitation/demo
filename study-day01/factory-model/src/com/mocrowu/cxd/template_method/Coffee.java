package com.mocrowu.cxd.template_method;

public class Coffee extends CaffeineBeverage {
    @Override
    public void brew() {
        System.out.println("冲咖啡。。。。");
    }

    @Override
    public void addCondiments() {
        System.out.println("加糖咖啡。。。。");
    }
}
