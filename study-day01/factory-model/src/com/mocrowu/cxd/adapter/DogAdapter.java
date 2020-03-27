package com.mocrowu.cxd.adapter;

/**
 * 适配器模式：
 *      适配器模式就是将一个类的接口转换为客户所期望的接口，兼容不同的接口
 *      客户端调用适配器的方法，适配器的方法内部进行转换
 *  客户端只能看到目标接口，而
 */
public class DogAdapter implements Robot {
    Dog dog;// 适配对象

    public DogAdapter(Dog dog) {
        this.dog = dog;
    }

    /**
     * 在对应的方法中进行转换
     */
    @Override
    public void cry() {
        System.out.println("机器人模仿狗叫。。。。");
        dog.cry();
    }

    @Override
    public void run() {
        System.out.println("机器人模仿狗跑。。。。");
        dog.run();
    }
}
