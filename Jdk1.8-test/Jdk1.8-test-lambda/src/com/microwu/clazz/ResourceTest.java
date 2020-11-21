package com.microwu.clazz;

import java.io.InputStream;
import java.net.URL;

/**
 * Description: 浅析 Java 中的资源加载
 *  1. 类加载器
 *      虚拟机设计团队把类加载阶段中的“通过一个类的全限定名来获取描述此类的二进制字节流”这个动作放到了 Java 虚拟机外部实现，以便让应用程序自己决定如何去获取所需要的类，
 *      而实现这个动作的代码模块称为“类加载器（ClassLoader）”
 *
 *      类加载器虽然只实现类加载的功能，但它在 Java 程序中起到的作用不局限于类加载阶段。对于任意一个类，都需要有加载它的类加载器和这个类本省一同确立类在 Java 虚拟机中的唯一性，
 *      每一个类加载器，都拥有一个独立的类命名空间。直观的说：比较两个类是否相等，只有在这两个是由同一个类加载器加载的前提下才有意义，否则这两个类必然不相等
 *
 *  2. 双亲委派模型
 *      启动类加载器 Bootstrap ClassLoader，C++ 实现，负责将存放在 ${JAVA_HOME}\bin 目录加载到虚拟机内存中
 *      扩展类加载器 Extension ClassLoader，sun.misc.Launcher ExtClassLoader 实现，负责 ${JAVA_HOME}\bin\ext 目录的所有类库
 *      应用程序类加载器 Application ClassLoader，有 sun.misc.Launcher AppClassLoader 实现，也叫做系统类加载器。它负责加载用户类路径上的类库
 *
 *      如果一个类加载器收到类加载请求，首先把这个请求委派给付磊加载器完成，因此所有类加载请求最终都应该传到顶层的类加载器，只有当父类加载器无法加载，子类加载器才会尝试自己去加载。
 *      每一个类加载器都会缓存已经加载过的类。
 *
 *      if (parent != null) {
 *          c = parent.loadClass(name, false);
 *      } else {
 *          c = findBootstrapClassOrNull(name);
 *      }
 *
 *  3. ClassLoader 资源加载
 *      getResource - file:/E:/work-note/Products/Jdk1.8-test/out/production/Jdk1.8-test-lambda/
 *      |- 类似双亲委派模型进行资源加载，基于用户应用程序的 ClassPath 搜索资源，资源名称必须使用路径分隔符 '/' 分割目录，不能以 '/' 起始
 *      getSystemResource
 *      |- 退化为 getResource 方法
 *
 *   4. Class 资源加载
 *      getResource
 *      |- 底层还是使用 classLoader.getResource
 *      getResourceAsStream
 *      |- 多了一步 resolveName，如果以 '/' 开头，退化为 ClassPath 查找；如果不是以 '/' 开头，将包路径替换为 '/'
 *          如果以 '/' 开头，就从 ClassPath 加载；否则基于当前类的包目录加载
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/27   15:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ResourceTest {

    public static void test() {
        ClassLoader classLoader = ResourceTest.class.getClassLoader();
        System.out.println(classLoader.getParent().getParent());
        System.out.println(classLoader.getParent());
        System.out.println(classLoader);

        URL resource = classLoader.getResource("");
        System.out.println(resource);

        Class<ResourceTest> aClass = ResourceTest.class;
        InputStream resourceAsStream = aClass.getResourceAsStream("");


        URL url = ClassLoader.getSystemResource("");
        System.out.println(url);
    }

    public static void main(String[] args) {
        test();
    }

}