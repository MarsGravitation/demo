package com.mocrowu.cxd.adapter;

public class Client {
    public static void main(String[] args) {
        Robot robot = new BioRobot();
        Dog dog = new Dog();

        Robot dogAdapter = new DogAdapter(dog);
        dogAdapter.cry();
        dogAdapter.run();
    }
}
