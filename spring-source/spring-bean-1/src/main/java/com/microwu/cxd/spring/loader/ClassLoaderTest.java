package com.microwu.cxd.spring.loader;

import com.microwu.cxd.spring.resource.ClassResourceTest;
import sun.misc.Launcher;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Description: 类加载器及其对应的加载路径
 *  Path 与 CLASSPATH
 *      - Windows 环境变量中的Path 是用来指明Windows 的可执行文件的路径
 *      在安装 JDK 时，我们通常需要在Path 的环境变量中添加Java 的可执行文件路径%JAVA_HOME%\bin，
 *      这样我们可以在命令行直接使用Java、javac 等命令，而无需添加这些可执行文件的路径
 *
 *      - CLASSPATH 环境变量用于指定加载class 文件时的搜索路径
 *
 *  Java 的3 种类加载器及其对应的加载路径
 *      - 启动类加载器，扩展类加载器，应用程序类加载器
 *
 * https://blog.csdn.net/CurryXu/article/details/81632065
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/3   15:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassLoaderTest {

    static {
        System.out.println("执行了静态代码块");
    }

    public static void test() {
        System.out.println("BootstrapClassLoader 的加载路径：");
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urLs) {
            System.out.println(url);
        }

        System.out.println("============================================");

        // 扩展类加载器
        URLClassLoader extClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader().getParent();
        System.out.println(extClassLoader);
        System.out.println("扩展类加载器 的加载路径：");

        urLs = extClassLoader.getURLs();
        for (URL url : urLs) {
            System.out.println(url);
        }

        System.out.println("===============================================");

        // 取得应用类加载器
        URLClassLoader appClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        System.out.println(appClassLoader);
        System.out.println("应用/系统类加载器 的加载路径：");
        urLs = appClassLoader.getURLs();
        for (URL url : urLs) {
            System.out.println(url);
        }

        // 获取本类的类加载器
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader);

        // 应用/系统类加载器 也就是加载用户自己的类是用的同一个类加载器
        ClassLoader classLoader1 = ClassResourceTest.class.getClassLoader();
        System.out.println(classLoader1);
    }

    /**
     * Class.forName() 加载类和使用ClassLoader 加载类的区别
     *
     * Class.forName() 和 ClassLoader 都可以对类进行加载。ClassLoader 就是遵循双亲委派模型最终调用启动类加载器的类加载器，
     * 实现的功能是 - 通过一个类的全限定名来获取描述此类的二进制字节流，获取二进制流后放到JVM 中。Class.forName() 方法
     * 实际上也是调用 ClassLoader 来实现的
     *
     * Class.forName 底层是调用
     * forName0(className, true, ClassLoader.getClassLoader(caller), caller)
     * 第二个参数默认设置为 true，代表会执行类中的静态代码块，以及对静态变量赋值等操作
     *
     * 结论：Class.forName 加载时将类进行了初始化，而ClassLoader 并没有对类进行初始化，只是将类加载到虚拟机中
     *
     * https://mp.weixin.qq.com/s?__biz=MzAxNjk4ODE4OQ==&mid=2247489467&idx=2&sn=a51c1ea98a8e64fc8c8453867c0e1045&chksm=9bed36c9ac9abfdffeb93dc09c98af601151cf47160cdb28911ba04988ea18fe7e22184071e2&scene=126&sessionid=1591579066&key=dca7f6c67ae3f
     * c75c1f878d696aa8ea56f3c04f831dfe901dc784491cbcf2033992a6f836ece074dda73e7415c0807041647674d91903c95bb2dbc48fc61d1c22f74a67b579a123bf5761f4cb8f55640&ascene=1&uin=MTAwMTU2MDQyOA%3D%3D&devicetype=Windows+10+x64&version=6209007b&lang=zh_CN&exportkey=AzwBZ1NIYFdWDmcfdORh5Do%3D&pass_ticket=r%2BTQbwifafoSULniV8DQdrSvCSx8QjAGvG34xvCEhHrbSKW5JOVQbyorP6imxsgX
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/8  9:50
     *
     * @param   	
     * @return  void
     */
    public static void test02() throws ClassNotFoundException {
        System.out.println("Class.forName。。。。");
        Class.forName("com.microwu.cxd.spring.loader.ClassLoaderTest");

    }

    /**
     * 这个不太好测试，我估计需要到另一个类中进行测试，因为调用 static 方法时，已经将类初始化了
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/8  10:06
     *
     * @param
     * @return  void
     */
    public static void test03() throws ClassNotFoundException {
        System.out.println("ClassLoader。。。。");
        ClassLoader.getSystemClassLoader().loadClass("com.microwu.cxd.spring.loader.ClassLoaderTest");
    }

    public static void main(String[] args) throws ClassNotFoundException {
        test();
//        test02();
//        test03();
    }
}