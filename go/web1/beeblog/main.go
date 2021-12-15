package main

/*
框架的数据流

1. main.go 作为应用入口，初始化一些运行博客所需要的基本资源，配置信息，监听端口
2. 路由功能检查 HTTP 请求，根据 URL 以及 method 来确定控制层来处理请求的转发
资源
3. 如果缓存文件存在，它将绕过通常的流程执行，被直接发送给浏览器
4. 安全检测，应用程序控制器调用之前，HTTP 请求和任意用户提交的数据将被过滤
5. 控制器装载模型、核心库、辅助函数，以及任何处理特定请求所需的其他资源，控制器
主要负责处理业务逻辑
6. 输出视图层中渲染好的即将发送到 Web 浏览器中的内容。如果开启缓存，视图首先被
缓存，将用于以后的常规请求

目录结构

|- main.go 入口文件
|- conf 配置文件和处理模块
|- controllers 控制器入口
|- models 数据库处理模块
|- utils 辅助函数库
|- static 静态文件目录
|- views 视图库
 */
