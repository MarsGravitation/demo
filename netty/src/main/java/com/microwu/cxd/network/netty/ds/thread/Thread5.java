package com.microwu.cxd.network.netty.ds.thread;

/**
 * Netty 的接入线程池和服务端 Channel 是如何关联的
 *
 * Selector 使得一个单独的线程能轻松管理 N 个 Socket，从而管理多路网络连接，
 * 也就实现了所谓的 I/O 多路复用，一句话 --- 多路网络复用一个 I/O 线程
 *
 * Selector selector =  Selector.open(); // 创建一个 I/O 多路复用器
 *
 * 其次，创建好 selector 需要一个 channel 组件去和这个 selector 关联，因为需要通过它
 * 接受外部的新连接请求，这个过程术语叫向 selector 注册服务端的 Channel，可以简单理解为
 * 在某个给定的 selector 上附加一个 Socket，还得知道一个细节，即 Channel 与 Selector
 * 一起搭配时，Channel 必须处于非阻塞模式
 *
 * channel.configureBlocking(false);
 * SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
 *
 * SelectionKey 代表了一个注册到某个 Selector 的 Channel
 *
 * key 包含了一些属性：
 *  interest 集合：为 Channel 注册的感兴趣的 I/O 事件集合 - 可以同时注册多个类型的 I/O 事件
 *  int interestSet = selectionKey.interestOps(); // 读写 interest 集合
 *
 *  int readySet = selectionKey.readyOps(); // ready 集合
 *
 *  Channel channel = selectionKey.channel(); // 关联的 Channel
 *  Selector selector = selectionKey.selector(); // 关联的 selector
 *
 *  selectorKey.attach(object); // 附加的对象
 *  Object attachedObj = selectionKey.attachment();
 *
 *  Netty 就是直接将自己封装的 Channel 对象，作为附加绑定到了 I/O 多路复用器上
 *
 *  服务端 NioServerSocketChannel 实例化时，会将 JDK 的 Channel 创建和初始化，在 initAndRegister 内实现。
 *  除此之外还会通过 bossGroup 调用 register 方法，将 bossGroup 和 NioServerSocketChannel 关联
 *      1. 注册 I/O 多路复用器
 *      selectionKey = javaChannel().register(eventLoop().selector, 0, this);
 *      2. 传播 handlerAdded 的事件
 *      3. 传播 Channel 注册 I/O 多路复用器成功的事件
 *      4. 如果当前 Channel 已经绑定到底层端口，那么还会传播 handlerActive 事件
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484542&idx=1&sn=bfa8c7bf2dfb6c3edf19f6598a13ee77&chksm=fbc0927eccb71b68113a40c5b0d2ef955508ccc598465bb30daab6a70e56470af4f500250011&scene=21#wechat_redirect
 *
 * Netty 服务端底层绑定端口并设置监听事件的过程分析
 *
 * 总结：
 * 在端口绑定完成的时候，为服务端 Channel 设置 OP_ACCEPT 事件。
 * 细节是在绑定端口后会传播 active 事件，从头结点开始到节点，传播完毕继续调用头结点触发 read 事件，传播出站事件，
 * 最后调用了头结点的 read 方法，里面有一个 doBeginRead 方法会绑定 OP_ACCEPT 事件，以后有新连接接入，Selector
 * 就能轮询到，然后将新连接交给 Netty 做下一步的处理。
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247485218&idx=2&sn=07f0e0df35f6cdab1db200b6495d443b&chksm=fbc09122ccb718340d20b085c1653c22cae3484433da495dd731762b424d7f2f9926247d359e&scene=21#wechat_redirect
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/19  15:24
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class Thread5 {

    public static void main(String[] args) {

    }

}
