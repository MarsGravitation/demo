package com.microwu.cxd.controller.http;

/**
 * Description:
 *
 * https://juejin.cn/post/7182460718506049594
 *
 * 2.1 自定义注解
 *  a. 通过自定义注解，将一些通用参数信息直接附加再接口上，达到接口即文档的效果
 *  b. 新增方法时，按文档接口内容，简单配置即可使用
 *  c. 接口代码变得简洁，减少样板代码
 *
 * 2.2 动态代理增强接口方法
 *  a. 通过动态代理，可以屏蔽这些复杂或存在差异的实现细节，让使用者面向纯接口编程
 *  b. 结合注解，代理类实现无入侵式的代码扩展
 *
 * 2.3 将代理类 Bean 注入到 Spring 容器
 *  a. 支持 Spring IOC 特性
 *  b. 保证代理实现和普通接口实现的调用方式无差别，用户无感知
 *
 * 3. 实现
 *  a. 在 Spring 启动初始化时，通过 @Import({HTTPMethodScannerRegistrar.class}) 来驱动
 *      ImportBeanDefinitionRegistrar 接口的实现类进行定制化 Bean 的注册
 *  b. 实现 ImportBeanDefinitionRegistrar 接口的 registerBeanDefinitions 方法，只要是获取
 *      带有 @HTTPController 注解的接口，使用这些接口的元数据的注解信息来构建
 *      HTTPControllerFactoryBean 的 Bean，然后注册进 Spring 容器中
 *  c. 从 HTTPControllerFactoryBean 中实际获取的 Bean，是调用 实现 FactoryBean 接口的
 *      getObject() 方法 获取的，该方法就是使用 Proxy.newProxyInstance 来实例化代理类，
 *      从而达到将目标接口的增强 Bean 注册到 Spring 容器中
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   10:19
 * Update History:
 * Author        Time            Content
 */
public class HttpAnnotationController {
}
