package com.mocrowu.cxd.simple;

public class EnglishPizza extends Pizza{
    @Override
    public void prepare() {
        System.out.println("准备英国披萨！！！");
    }

    @Override
    public void bake() {
        System.out.println("烤英国披萨！！！");
    }

    @Override
    public void cut() {
        System.out.println("切英国披萨！！！");
    }

    @Override
    public void box() {
        System.out.println("将英国披萨装盒！！！");
    }
}
