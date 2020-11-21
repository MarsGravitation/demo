package com.microwu.cxd;

/**
 *
 * 1. SpringMVC 的运行流程
 *  a. 用户发送请求至前端控制器 DispatcherServlet
 *  b. DispatcherServlet 收到请求调用 HandlerMapping 处理器映射器
 *  c. 处理器映射器根据请求 URL 找到具体的处理器，生成处理器对象以及处理器拦截对象一并返回给 DispatcherServlet
 *  d. DispatcherServlet 通过 HandlerAdapter 处理器适配器调用处理器
 *  e. 执行处理器（Controller，也叫后端控制器）
 *  f. Controller 执行完成翻译 ModelAndView
 *  g. HandlerAdapter 将 controller 执行结果 ModelAndView 返回给 DispatcherServlet
 *  h. DispatcherServlet 将 ModelAndView 传给 ViewReslover 视图解析器
 *  i. ViewReslover 解析后返回具体 View
 *  j. DispatcherServlet 对 View 进行渲染视图，将模型数据填充至视图
 *  k. DispatcherServlet 响应用户
 *
 * 2. 其他
 *  RequestMapping：支持类和方法
 *      - 用于映射处理前端发送过来的 URL
 *      - 同时用于类和方法上，那么请求路径就是两者的路径使用 / 分隔开
 *      - 路径支持通配符 (? * **)
 *  PathVariable 和 RequestParam
 *      - PathVariable 映射 URL 绑定的占位符，一般通过 {xxx} 占位
 *      - RequestParam 取出来的是请求参数
 *          |- 如果 @RequestParam 参数不存在，报 400，可以设置 required = false
 *  RequestHeader 和 CookieValue
 *  POJO 绑定请求参数
 *      spring MVC 支持按照请求参数名和 pojo 属性进行自动匹配，自动为该对象填充属性，支持级联
 *      支持 Servlet 原生 API - request, response
 *  处理模型数据
 *      - ModelAndView：Spring 会把 ModelAndView 的 Model 放入 request 域中
 *      - Map：入参位置可以添加一个 Map，mvc 会把隐藏的模型引用传递给这个入参，我认为是把参数封装到 Map 中
 *   SessionAttributes
 *      - 它会把我们存放到作用域中的信息备份到 session 中
 *   数据校验：当我们添加 <mvc:annotation-driven/>，springMVC 会自动我们做以下处理：
 *      RequestMappingHandlerMapping
 *      RequestMappingHandlerAdapter
 *      ExceptionHandlerExceptionResolver
 *      支持使用 ConversionService 对表单参数进行类型转换
 *      支持使用 @NumberFormatanptation @DateTimeFormat 完成数据类型格式化
 *      支持使用 @Valid 对实例进行 jsr 验证
 *      支持使用 @RequestBody 和 ResponseBody 处理 ajax
 *  拦截器：
 *      第一个返回 false，其他拦截器不执行，目标方法不执行
 *      第一个返回 true，第二个返回 false，目标方法不执行，但是第一个拦截器的 afterCompletion 会执行
 *  ExceptionHandlerException：
 *      处理优先级，找离异常最近的异常
 *
 * https://my.oschina.net/liughDevelop/blog/1622646
 * https://www.cnblogs.com/ZhuChangwu/p/11704917.html
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
