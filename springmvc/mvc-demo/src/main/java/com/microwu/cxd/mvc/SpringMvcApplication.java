package com.microwu.cxd.mvc;

/**
 * Description:
 *  一、spring MVC 入门
 *  1. web.xml 中配置入口 servlet
 *      初始参数一：contentConfigLocation - 上下文配置文件路径
 *      初始参数二：主要是配置 springMVC 的一些配置，例如 Controller 的初始化，静态文件的映射策略，视图配置等
 *
 *  2. springmvc.xml
 *      开启包扫描配置
 *
 * 二、DispatcherServlet 源码分析
 *  1.  初始化过程 - 入口 init 方法
 *      a. HttpServletBean - 将 web.xml 中配置的参数设置到 Servlet 中。比如 init-param 标签
 *      b. FrameworkServlet - 将 Servlet 与 Spring 容器上下文关联
 *      c. DispatcherServlet - 初始化各个功能的实现类。比如异常处理，视图处理，请求映射处理等等
 *  2. DispatcherServlet 处理请求过程
 *      doDispatch 方法
 *          |- 根据请求路径找到 HandlerMethod（带有 Method 反射属性，也就是对应 Controller 中的方法）
 *          |- 匹配路径对应的拦截器
 *          |- HandlerMethod + 拦截器构造个 HandlerExecutionChain 对象
 *          |- 通过 HandlerAdapter 对象进行处理得到 ModeAndView 对象
 *          |- HandlerMethod 内部 handle 的时候，使用各种 HandlerMethodArgumentResolver 实现类处理 HandlerMethod 参数，是哟个 HandlerMethodReturnValueHandler 处理返回值
 *          最终生成 ModelAndView 对象，异常会被 HandlerExceptionResolver 处理
 *
 *  三、如何找到 Controller
 *      HandlerMethod：封装了方法参数、方法注解、方法返回值等
 *      MethodParameter：封装了方法参数具体信息的工具类
 *      RequestCondition：
 *
 *
 * https://www.cnblogs.com/ZhuChangwu/p/11704904.html
 * https://www.cnblogs.com/fangjian0423/p/springMVC-introduction.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/6   15:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SpringMvcApplication {

    public static void main(String[] args) {
//        ContextLoaderListener
//        DispatcherServlet
//        AnnotationDrivenBeanDefinitionParser
//        MethodIntrospector
//        RequestMappingHandlerMapping
//        RequestMappingHandlerAdapter
//        HandlerMethod
//        AbstractHandlerMethodMapping
    }

}