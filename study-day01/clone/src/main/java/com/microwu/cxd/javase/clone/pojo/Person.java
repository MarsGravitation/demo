package com.microwu.cxd.javase.clone.pojo;

public class Person implements Cloneable{
    private int age;
    private String name;

    public Person() {
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public  Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
