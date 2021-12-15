package com.microwu.clazz;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Class
 * 1. 简介
 *  每个类的运行时的类型信息用 Class 对象表示。它包含了与类有关的信息。其实我们的实例对象是通过 Class 对象创建的。Java 使用 Class 对象执行其 RTTI
 *  （运行时类型识别：Run-Time Type Identification），多态就是基于 RTTI 实现的。
 *
 *  每个类都有一个 Class 对象，每当编译一个新类就产生一个 Class 对象，基本类型（boolean，int）有 Class 对象，数组有 Class 对象，void 也有 Class 对象。
 *  Class 对象对应着 java.lang.Class 类，类是对象抽象和集合，Class 是类的抽象和集合
 *
 *  Class 类没有公共的构造方法，Class 对象是在类加载的时候由 Java 虚拟机以及通过调用类加载器的 defineClass 方法自动构造的，因此不能显示的声明一个 Class 对象。
 *  一个类被加载到内存经历三个阶段：
 *      加载，由类加载器执行的，通过一个类的全限定名来获取其定义的二进制字节流（Class 字节码），将这个字节流所代表的静态存储结构转换为方法区的运行时数据接口，
 *      根据字节码在 Java 堆中生成一个代表类的 java.lang.Class 对象
 *      链接，验证 Class 文件中的字节流包含的信息是否符合当前虚拟机的要求，为静态域分配存储空间并设置类变量的初始值，如果必需的时候，将常量池中的符号引用转换为直接引用
 *      初始化，执行类中定义的 Java 程序代码。执行类的静态初始器和静态初始块，如果该类有父类，优先对其父类进行初始化
 *
 *  所有的类都是在其第一次使用的时候，动态加载到 JVM 中
 *  当程序创建第一个对静态成员的引用时，会加载这个类
 *  使用 new 创建类对象的时候也被当作对类的静态成员的引用
 *
 *  先在内存中创建这个类的 Class 对象，然后再用这个 Class 对象创建具体的实例对象。
 *
 * 2. 获得 Class 对象
 *  Class.forName(String name, boolean initialize, ClassLoader loader)：initialize = false 在加载类时不会立即运行静态区块，创建对象时才会运行
 *  类名.class
 *  实例对象.getClass()
 *
 *  Class.forName().newInstance() 和 new 关键字的异同：前者利用类加载机制，后者是创建一个新类
 *  newInstance 把 new 分解为两步，首先调用 Class 加载类，然后实例化
 *
 * 3. 获取类型的信息
 *  getClassLoader()：获取该类的类装载器
 *  getName():String：获得该类的完全名称
 *
 *  getSupperClass：获取超类
 *  getDeclaredFields：获取所有成员变量，返回 Field 对象的一个数组，返回 Class  对象所声明的所有字段。包括共有，保护，但不包含继承的字段。
 *  返回数组中的元素没有排序，也没有特定的顺序。
 *  getDeclaredField：获取指定名称的成员变量
 *  getDeclaredMethods：获取所有成员方法
 *  getDeclaredMethod：获取指定名称的成员方法
 *
 *
 *  https://blog.csdn.net/weixin_43653599/article/details/105941605
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/27   10:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassTest {

    public static void main(String[] args) throws ClassNotFoundException {
//        String string = new String();
//        ClassLoader classLoader = string.getClass().getClassLoader();
//        classLoader.loadClass("");

        test();
    }

    /**
     * JAVA 中 isAssignableFrom()方法与 instanceof 关键字 区别及使用
     *
     * isAssignableFrom:　是从类继承的角度去判断的，判断是否为某个类的父类
     *
     * 父类.class.isAssignableFrom(子类.class)
     *
     * instanceof: 从实例继承的角度去判断的，instanceof 判断是否为某个类的子类
     *
     * 子类实例 instanceof 父类类型
     *
     * https://www.cnblogs.com/pythoncd/articles/12545666.html
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/9     15:05
     *
     * @param 
     * @return void
     */
    public static void test() {

        // 父类.class.isAssignableFrom(子类.class)
        // 判断是否为某个类的父类
        System.out.println(List.class.isAssignableFrom(ArrayList.class));

        ArrayList<Object> list = new ArrayList<>();
        System.out.println(list instanceof List);

    }

}