package com.microwu.cxd.network.netty.ds.thread;

/**
 *
 * Reactor 模型
 *
 *  1. 单线程模型
 *  2. 多线程模型
 *  3. 主从多线程模型
 *
 *  Netty 使用的是 Reactor 多线程模型，一般情况下，服务器感兴趣的 I/O 事件一般分为
 *  新连接到达事件、某个连接上的 I/O 可读事件和 I/O 可写事件。Netty 把新连接到达事件
 *  单独用一个线程处理，即充当主 Reactor 线程的角色，而 I/O 可读和 I/O 可写事件放到
 *  另外一组 NIO 线程里处理，即从 Reactor 线程
 *
 *  从 Reactor 默认为 2 * CPU
 *
 *  模型的运行规则：在运行时主 Reactor 只处理 accept 事件，新连接到来马上按照一定策略，
 *  将其转发给 Reactor 线程池，在该线程池中按照策略挑选一个线程来处理这个新连接上的 I/O
 *  事件，故主 Reactor 的开销非常小，也不会阻塞。而每个子 Reactor 可以管理多个网络连接，
 *  负责这些连接的 I/O 读和 I/O 写，因为是 NIO，所以也不会阻塞，如果某个连接上有非 I/O
 *  的耗时业务逻辑，那么 NIO 线程在读到完整的网络消息后，将其丢给非 I/O 线程池处理，处理完
 *  后复杂业务逻辑的响应消息一般会放到一个响应队列，从 Reactor 会按照一定策略去处理响应队列，
 *  然后将消息写回客户端
 *
 *  对于只有一个服务端口的应用，bossGroup 设置多个线程没啥用，一般设置为 1 即可
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484674&idx=5&sn=ab1ca43577d1effb9d2675af38ae1665&chksm=fbc09302ccb71a14b883952353be75dfb8ab1cb80d9e7d6f73b0d90e51f61b8f861e6b9c0f06&scene=21#wechat_redirect
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/16  20:05
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class Thread1 {
}
