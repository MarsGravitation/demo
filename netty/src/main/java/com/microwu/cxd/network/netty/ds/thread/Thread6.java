package com.microwu.cxd.network.netty.ds.thread;

/**
 * Netty 如何处理新连接接入事件
 *
 * 宏观印象：
 *  1. 通过 I/O 多路复用器 - Selector 检测客户端新连接
 *      新连接通过服务端的 NioServerSocketChanel 绑定的 I/O 多路复用器（有 NioEventLoop 线程驱动）
 *      轮训 OP_ACCEPT 事件
 *  2. 轮训到新连接，就创建客户端的 Channel
 *  3. 为新连接分配绑定新的 Selector
 *      通过线程选择器，从它的第二个线程池 - worker 线程池中挑选一个 NIO 线程，
 *      在这个线程中去执行将 SocketChannel 注册到新的 Selector 的流程，将
 *      Netty 封装的 NioSocketChannel 作为附加对象也绑定到该 Selector
 *  4. 向客户端 Channel 绑定的 Selector 注册 I/O 读、或者写事件
 *      默认注册读事件。以后本条 Channel 的读写时间就由 worker 线程池中的 NIO 线程管理
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484542&idx=1&sn=bfa8c7bf2dfb6c3edf19f6598a13ee77&chksm=fbc0927eccb71b68113a40c5b0d2ef955508ccc598465bb30daab6a70e56470af4f500250011&scene=21#wechat_redirect
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/19  15:07
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class Thread6 {
}
