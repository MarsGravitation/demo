package com.mocrowu.cxd.prototype;

/**
 *  原型模式就是Java中的克隆，实现一个cloneable接口，但是存在深克隆和浅克隆的问题
 *  深克隆需要比较复杂的代码
 */
public class Client {
    public static void main(String[] args) {
        //原型A对象
        Resume a = new Resume("小李子");
        a.setPersonInfo("2.16", "男", "XX大学");
        a.setWorkExperience("2012.09.05", "XX科技有限公司");

        //克隆B对象
        Resume b = (Resume) a.clone();

        //输出A和B对象
        System.out.println("----------------A--------------");
        a.display();
        System.out.println("----------------B--------------");
        b.display();


        System.out.print("A==B?");
        System.out.println( a == b);


        System.out.print("A.getClass()==B.getClass()?");
        System.out.println(a.getClass() == b.getClass());
    }
}
