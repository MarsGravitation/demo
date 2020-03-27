package com.microwu.inner;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Java 有三种类型的内部类
 *  1. 普通内部类
 *  2. 匿名内部类
 *  3. 方法局部内部类
 *
 * 一个类暴露了一个addJob 方法, 一个参数 task, 类型是Runnable, 然后定义了一个内部类
 * Job 类对task 进行了一层封装, 这里Job 是私有的, 这里是内部类的第一个优势
 *  |- 内部类能够更好的封装, 内聚, 屏蔽细节
 *  |- 内部类天然有访问外部类成员变量的能力
 *
 * 为什么内部类, 会持有外部类的引用
 *  首先会给内部类重新生成一个class文件, 构造参数为外部类, 保存到this$0字段中
 *
 * 为什么匿名内部类使用到外部类中的局部变量时需要是final类型的?
 *  对于非final 的局部变量,是通过构造函数传递进来的, 而final 的局部变量, 编译期就可以确定
 *  从上面的例子可以看到, 不一定需要局部变量是final, 但是你不能在内部类中修改外部类的局部变量,
 *  因为Java 对于匿名内部类传递变量的实现是基于外部局部变量副本, 最终并不会对外部类产生效果,
 *  因为已经是第二个变量了. 这样就会让程序员产生困扰, 原以为修改会生效, 事实上并不会
 *
 *  但是可以修改外部类成员变量
 *
 * 如果创建内部类实例 - 由于内部类对象需要持有外部类对象的引用, 所以必须得先有外部类对象
 *
 * 引申的问题, 基本类型和引用类型的理解
 *  1. 基本类型的变量保存原始值, 所以变量就是数据本身
 *  2. 引用类型的变量保存引用值, 所谓的引用值就是对象所在内存空间的"首地址值", 通过对这个引用值来操作对象
 *
 *  值传递, 引用传递的理解
 *      1. 在方法调用过程中, 实参把它的实际值传递给形参, 此传递过程就是将实参的值复制一份传递到函数中, 这样
 *      如果在函数中对该值进行了操作不会影响到实参的值. 因为是直接复制, 所哟这种方式在传递大量数据时, 运行效率
 *      会特别地下.
 *      2. 引用传递. 为了弥补值传递的不足, 引用传递是将对象的地址值传递过去, 函数接受的是原始值的首地址值. 在方法
 *      的执行过程中, 形参和实参的内容相同, 指向同一块内存地址, 所以方法的执行会影响到实际对象
 *
 *      注意: String, Integer特殊, 可以理解为传值
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/10/24   16:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class InnerClassDemo {
    private List<Job> strings = new ArrayList<>();

    public void addString(Runnable task) {
        strings.add(new Job(task));
    }

    private class Job implements Runnable {
        Runnable task;
        public Job(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            System.out.println("left job size: " + strings.size());
        }
    }

    public static void swap(int a, int b) {
        int t = a;
        a = b;
        b = t;
    }

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        swap(1, 2);
        System.out.println(a + "," + b);
    }

}