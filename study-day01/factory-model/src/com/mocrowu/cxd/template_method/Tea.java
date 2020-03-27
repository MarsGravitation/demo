package com.mocrowu.cxd.template_method;

public class Tea extends CaffeineBeverage {
    @Override
    public void brew() {
        System.out.println("泡茶。。。。");
    }

    @Override
    public void addCondiments() {
        System.out.println("加水。。。。");
    }
}
