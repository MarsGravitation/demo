package com.mocrowu.cxd.abstract_factory.pojo;

public abstract class Pizza {
    protected String name;
    protected Dough dough;
    protected Sauce sauce;
    protected Veggies[] veggies;
    protected Cheese cheese;
    protected Pepperoni pepperoni;
    protected Clams clams;

    protected abstract void prepare();
    protected void bake(){
        System.out.println("烤");
    }
    protected void cut(){
        System.out.println("切");
    }
    protected void box(){
        System.out.println("打包");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
