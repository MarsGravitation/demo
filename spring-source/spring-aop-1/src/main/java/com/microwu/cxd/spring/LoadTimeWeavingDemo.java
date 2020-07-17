package com.microwu.cxd.spring;

/**
 * Description: 如果熟悉aop，会知道aop 的原理是基于 beanPostProcessor的。比如平时我们会在 service 类的部分方法上加 @Transactional
 *      transactional 是基于 aop 实现的。最终结果就是，注入到 controller 层的 service，并不是原始的 service bean，而是一个动态代理对象，
 *      这个动态代理对象，会在执行你真正的 service 方法前后，去执行事务的打开和关闭
 *
 *      aop 的限制就在于：被 aop 的类，需要被 spring 管理，管理的意思就是，需要通过 @Component 弄成一个 bean
 *      假设我们想要在一个第三方，没被spring 管理的类的一个方法前后，做一些 aop 的事情，该怎么办？
 *
 *      一般来说，目前的方法主要是通过修改 class 文件
 *      class 文件在什么时候才真正生效？
 *      public Class<?> loadClass(String name) - 一旦通过上述方法，获取返回的 Class 对象后，基本就不可修改了
 *      那根据这个原理，大致有三个时间节点，对 class 进行修改：
 *      1. 编译器织入，比如 aspectJ 的ajc 编译器，假如你自己负责实现这个 ajc 编译器，你当然可以自己夹带私货，悄悄的往要编译的 class 文件里加点料，
 *      这样的话，编译出来的 class，和 java 源文件里的，其实是不一样的
 *      2. 自己实现 classloader，在调用上述的 loadClass(String name)时，自己加点料；通俗的说，就是 load-time-weaving，加载时织入
 *      其中，又分为两种，因为我们知道，classloader 去load class 的时候，其实是分两步的，一个是 java 代码层面，一个是 JVM层面
 *      java 代码层面：你自定义的 classloader，针对传进来的 class，获取其 inputStream后，对其进行修改后，再丢给 JVM 去加载为一个 Class
 *      JVM 层面： Instrumentation 机制，具体理论的东西不是特别清楚，简单来说，就是 Java 命令启动时，指定 agent 参数，agent jar 里面有一个 permain 方法，
 *      该方法可以注册一个字节码转换器
 *
 *  第一种，需要使用 aspectj 的编辑器来进行编译，还是略显麻烦
 *  第二种，LTW，包含两个部分，一部分是切面问题（切点定义切哪儿，通知定义在切点处要嵌进去的逻辑），一部分是切面怎么生效的问题
 *
 *  Aspectj 官网 - https://www.eclipse.org/aspectj/doc/released/devguide/ltw-configuration.html
 *      第一种：就是我们前面说的 Java instrumentation，只是这里的 agent 是使用 aspectjweaver.jar
 *      第二种：使用了专有命令来执行
 *      第三种：自定义 classloader 方式
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/3   9:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LoadTimeWeavingDemo {
    public static void main(String[] args) {
        StuEntitlementCalculationService stuEntitlementCalculationService = new StuEntitlementCalculationService();
        stuEntitlementCalculationService.calculateEntitlement();

//        ClassLoader
    }
}