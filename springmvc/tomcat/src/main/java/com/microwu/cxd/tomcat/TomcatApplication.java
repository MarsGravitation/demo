package com.microwu.cxd.tomcat;

/**
 * Description: Tomcat 整体架构及组件
 *  1. 整体架构图
 *      一个 Tomcat 只有一个 Server
 *      一个 Server 可以包含多个 Service
 *      一个 Service 只有一个 Container
 *      一个 Container 可以有多个 Connectors，因为一个服务可以有多个连接，如同时提供 HTTP 或者 HTTPS 连接
 *      最核心的两个组件 - 连接器（Connector）和容器（Container）
 *          |- Connector 用于处理连接相关的事情，并提供 Socket 和 Request、Response 相关的转换
 *          |- Container 用于封装和管理 Servlet，以及具体处理 Request 请求
 *
 *  2. 组件
 *      |- Serve 表示服务器，提供了一种优雅的方式来启动和停止整个系统，不必单独启停连接器和容器
 *      |- Service 表示服务，Server 可以运行多个服务，比如一个 Tomcat 里面可以运行订单服务，支付服务等等
 *      |- 每个 Service 可以包含 Connector 和 一个 Container。因为每个服务器允许同时支持多种协议，每个协议最终执行的 Servlet 是相同的
 *      |- Connector 表示连接器，比如一个服务可以同时支持 HTTP、HTTPS 协议，每种协议可以使用一种连接器支持
 *      |- Container 表示容器，可以看作 Servlet 容器
 *          - Engine 引擎
 *          - Host 主机
 *          - Context 上下文
 *          - Wrapper 包装器
 *      |- 管道组件和阀门组件（Pipeline - Valves）
 *
 *  3. Connector 和 Container 的微妙关系
 *      一个请求到达 Tomcat ，首先经过 Service 交给 Connector，Connector 用于接受请求并将请求封装为 Request 和 Response 来具体处理，封装好之后交给
 *      Container 进行处理，Container 处理完再返回给 Connector，最后由 Connector 通过 Socket 将处理的结果返回给客户端
 *
 *  4. Connector 架构分析
 *      Connector 使用 ProtocolHandler 来处理请求，比如 Http11Protocol 使用的是普通 Socket 来连接的
 *      ProtocolHandler 包含了三个部件：Endpoint、Processor、Adapter
 *      Endpoint 用来处理底层 Socket 网络连接
 *      Process 将 Socket 封装成 Http
 *      Adapter 将请求适配到 Servlet 进行具体处理
 *
 *  5. Container 如何处理请求
 *      Container 处理请求时使用 Pipeline-Valve 来处理的
 *      Pipeline-Value 是责任链模式，有一点区别
 *      a. Connector 首先会调用最顶层 Pipeline 来处理，就是 EnginePipeline
 *      b. 顺着管道最后执行到 StandardWrapperValve
 *      c. 当执行到 StandardWrapperValve 会创建 FilterChain，调用其 doFilter 来处理请求，doFilterChain 包含配置的 Filter 和 Servlet，
 *          doFilter 会依次执行 Filter 的 doFilter 和 Servlet 的 service 方法
 *      d. 当所有 Pipeline-Valve 都执行完，将结果返回给 Connector，Connector 通过 Socket 方式返回给客户端
 *
 * https://www.cnblogs.com/java-chen-hao/p/11316795.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/11   14:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TomcatApplication {
}