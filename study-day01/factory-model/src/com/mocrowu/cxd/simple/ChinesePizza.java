package com.mocrowu.cxd.simple;

public class ChinesePizza extends Pizza {
    @Override
    public void prepare() {
        System.out.println("准备中国披萨~~~");
    }

    @Override
    public void bake() {
        System.out.println("烤中国披萨~~~");
    }

    @Override
    public void cut() {
        System.out.print("切中国披萨~~~");
    }

    @Override
    public void box() {
        System.out.println("将中国披萨装盒~~~");
    }
}
