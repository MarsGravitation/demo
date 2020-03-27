package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 * 日志接口 slf4j
 *     slf4j 对所有日志框架指定的一种规范, 标准, 接口, 并不是一个框架的具体的实现, 因为接口并不能独立使用
 *     需要和具体的日志框架实现配合使用
 *
 * 日志实现 - log4j, logback, log4j2
 *      log4j 是apache实现的一个开源日志组件
 *      logback同样是由log4j的作者设计完成的, 拥有更好的特性, 用来取代log4j的一个日志框架, 是slf4j的原生实现
 *      log4j2 是log4j 和logback的改版, 据说采用了一些新技术(无锁异步), 使得日志的吞吐量, 性能比log4j 1.x 提高10倍,
 *      并解决了一些死锁debug, 而且配置更加简单灵活
 *
 * 为什么需要日志接口, 直接使用具体的实现不就行了吗
 *      接口用于定制规范, 可以有多个实现, 使用时是面向接口的 - 导入的包都是slf4j的包 而不是具体某个日志框架中的包,
 *      即直接和接口交互, 不直接使用实现, 所以可以任意的更换实现而不用改代码中的日志相关代码
 *
 *      比如: slf4j定义了一套日志接口, 项目中使用的是日志框架是logback, 开发中调用的所有接口都是 slf4j的, 不直接使用logback,
 *      调用是 自己的工程调用slf4j的接口, slf4j的接口去调用logback的实现, 可以看到整个过程应用程序并没有直接使用logback,
 *      当项目需要更换更加优秀的日志框架时, 只需引入log4j2的jar 和 配置文件即可, 也不用修改日志相关的类的导入的包
 *
 *      使用日志接口便于更换其他日志框架
 *      log4j, logback, log4j2 都是一种日志具体实现框架, 所以既可以单独使用也可以组合slf4f一起搭配使用
 *
 * log4j2 日志级别
 *      从大到小是: error, warn, info,debug, trace
 *
 *      对于Appender的理解: 简单地说Appender 就是一个管道, 定义了日志内容的去向(保存位置)
 *      配置一个或者多个Filter, Filter的过滤机制和Servlet的Filter 有些差别, 下文会进行说明
 *
 *      Layout 来控制日志信息的输出格式
 *      Policies 控制日志何时滚动
 *      Strategy 控制日志如何滚动
 *
 *      对于Logger的理解
 *
 *      简单地说Logger就是一个路由器, 指定类, 包中的日志信息流向那个管道, 以及控制它们的流量
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/26   10:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class SpringLoggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringLoggerApplication.class, args);

    }
}