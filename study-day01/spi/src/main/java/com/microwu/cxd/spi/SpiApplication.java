package com.microwu.cxd.spi;

import com.microwu.cxd.spi.service.IShout;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Description:
 *  SPI 全称 Service Provider Interface，是 Java 提供的一套用来被第三方实现或者扩展的 API，
 *  它可以用来启用框架扩展或替换组件
 *
 *  整体机制：
 *      调用方 ---> 标准服务接口 ---> 实现类（A,B,C）
 *  Java SPI 实际上是 基于接口的编程 + 策略模式 + 配置文件 组合实现的动态加载机制
 *
 *  系统设计的各个抽象，往往有很多不同的实现方案，在面向对象的设计里面，一般推荐模块之间
 *  基于接口编程，模块之间不对实体类进行硬编码。一旦代码里涉及具体的实现类，就违反了可插拔的原则，
 *  如果需要替换一种实现，就需要修改代码。为了实现在模块装配的时候能在程序里动态指明，这就需要一种服务发现机制
 *  Java SPI 就是提供这样的一个机制：为某个接口寻找服务实例的机制。有点类似于 IOC 思想，
 *  就是把装配的控制权移到程序之外，在模块化设计中这个机制由其重要。所以 SPI 的核心思想就是解耦
 *
 * 使用介绍：
 *  1. 当服务提供者提供了接口的一种具体实现后，在 jar 包的META-INF/services 目录下面创建一个
 *      以 接口全限定名 为命名的文件，内容为实现类的全限定名
 *  2. 接口实现类所在的jar 包放在主程序的 classpath 中
 *  3. 主程序通过 java.util.ServiceLoader　动态装载实现模块，它通过扫描 META-INF/service 目录下的
 *      配置文件找到实现类的全限定名，把类加载到 JVM
 *  4. SPI 的实现类必须携带一个不带参数的构造方法
 *
 * 源码解析：
 *  1. 先创建一个 ServiceLoader
 *  2. 通过迭代器获取对象实例
 *      - 读取 META-INFO/services/ 下的配置文件，获取所有类对象
 *      - 通过反射实例化对象
 *      - 放入缓存中 LinkedHashMap
 *
 * https://www.jianshu.com/p/46b42f7f593c
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/18   15:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SpiApplication {
    public static void main(String[] args) {
        ServiceLoader<IShout> shouts = ServiceLoader.load(IShout.class);
        Iterator<IShout> iterator = shouts.iterator();
        while (iterator.hasNext()) {
            iterator.next().shout();
        }

    }
}