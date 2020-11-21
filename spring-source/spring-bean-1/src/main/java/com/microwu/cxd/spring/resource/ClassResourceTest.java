package com.microwu.cxd.spring.resource;

import java.io.InputStream;
import java.net.URL;

/**
 * Description: 获取 classpath 路径下的资源文件
 * ClassLoader  提供了两个方法用于从装载的类路径上取得资源
 * public URL getResource(String name);
 * public InputStream getResourceAsStream(String name);
 * 这里 name 是资源的类路径，它是相对与 / 根路径下的位置
 * getResource 的带的是一个 URL 对象来定位资源，而 getResourceAsStream 取得该资源输入流的引用保证程序可以从正确的位置抽取数据
 * <p>
 * 但是真正使用的不是 ClassLoader 的这两个方法，而是 Class 的 getResource 和 getResourceAsStream 方法，因为 Class 对象可以从你的类得到，
 * 而 ClassLoader 则需要再调用一次 yourClass.getClassLoader() 方法，不过根据 JDK 文档的说法，Class 对象的这两个方法其实是委托 delegate 给
 * 装载它的 ClassLoader 来做的，所以只需要使用 Class 对象的这两个方法就可以
 *
 * https://www.cnblogs.com/diandianquanquan/p/11492925.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/3   14:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassResourceTest {

    /**
     * 总结：可以通过 class 获取类路径上的资源
     * 本质上都是通过 classloader 进行获取的，它获取的 class 路径 - 这里不要加 /，结果为 null
     * 而 Class.getResource 区分相对路径和绝对路径，相对路径就是 绝对路径 + 类名
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/6/3  14:56
     */
    public void test() {
        // 1. 得到的是当前类 class 文件的 URI 目录，不包括自己
        // file:/E:/work-note/Products/spring-source/spring-bean-1/target/classes/com/microwu/cxd/spring/resource/
        Class<? extends ClassResourceTest> aClass = this.getClass();
        URL resource = aClass.getResource("");
        System.out.println(resource);

        // 2. 得到的是当前的 classpath 的绝对 URI 路径
        // file:/E:/work-note/Products/spring-source/spring-bean-1/target/classes/
        URL resource1 = aClass.getResource("/");
        System.out.println(resource1);

        // 3. 得到的是当前 classpath 的绝对 URI 路径
        // file:/E:/work-note/Products/spring-source/spring-bean-1/target/classes/
        URL resource2 = aClass.getClassLoader().getResource("");
        System.out.println(resource2);

        // 4. 当前 classpath 的绝对路径
        // file:/E:/work-note/Products/spring-source/spring-bean-1/target/classes/
        URL resource3 = ClassLoader.getSystemResource("");
        System.out.println(resource3);

        // 5. 当前 classpath 的绝对路径
        // file:/E:/work-note/Products/spring-source/spring-bean-1/target/classes/
        URL resource4 = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(resource4);

        // 6. Web 应用中，得到 web 应用程序的根目录的绝对路径
        // ServletActionContext.getServletContext().getRealPath("/")

    }

    /**
     * 总结：classloader 只能使用 相对路径
     *      class 可以使用 绝对路径
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/3  15:07
     *
     * @param
     * @return  void
     */
    public void test02() {
        // 这个方法需要使用相对路径
        // 因为它底层用的还是 classloader 的 getResource，只能使用相对路径
        InputStream resourceAsStream = ClassResourceTest.class.getClassLoader().getResourceAsStream("a.txt");
        System.out.println(resourceAsStream);

        // null
        InputStream resourceAsStream1 = ClassResourceTest.class.getClassLoader().getResourceAsStream("/a.txt");
        System.out.println(resourceAsStream1);
    }

    public static void main(String[] args) {
        ClassResourceTest classResourceTest = new ClassResourceTest();
        classResourceTest.test();

        System.out.println();
        classResourceTest.test02();
    }
}