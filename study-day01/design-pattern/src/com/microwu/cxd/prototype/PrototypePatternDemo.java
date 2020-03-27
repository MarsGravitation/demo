package com.microwu.cxd.prototype;

/**
 * Description:
 *      意图： 用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象
 *      关键代码：实现克隆操作，在Java继承Cloneable，重写clone()
 *      使用场景：1.资源优化场景
 *              2.累初始化需要消耗非常多的资源，这个资源包括数据、硬件资源等
 *              3.性能和安全要求的场景
 *              4.通过new产生一个对象需要非常繁琐的数据准备和访问权限，则可以使用原型模式
 *              5.一对象多个修改者的场景
 *              6.一个对象需要提供给其他对象访问，而且各个调用者可能都需要修改其值时，可以考虑使用原型模式拷贝多个对象工调用者使用
 *              7.在实际项目中，原型模式很少单独出现，一般是和工厂方法模式一起出现，通过clone的方法创建一个对象，然后有工厂方法提供给调用者
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/4   10:08
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PrototypePatternDemo {
    public static void main(String[] args) {
        ShapeCache.loadCahche();
        Rectangle rectangle = (Rectangle) ShapeCache.getShape("1");
        System.out.println(rectangle.getType());

    }
}