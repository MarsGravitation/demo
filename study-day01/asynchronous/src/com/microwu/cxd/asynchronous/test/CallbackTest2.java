package com.microwu.cxd.asynchronous.test;

import com.microwu.cxd.asynchronous.client.A;
import com.microwu.cxd.asynchronous.server.B;

/**
 * Description:    在正常的业务中使用同步线程，如果服务器没处理一个请求，就创建一个线程的话，会对服务器资源造成浪费。
 *      因为这些线程可能会浪费时间在网络传输上、等待数据库连接等其它事件上，真正处理业务逻辑的时间很短很短，但是其他线程在线程池满了
 *      之后就会阻塞，等待前面的线程处理完毕。而且，会出现一个奇怪的现象，客户端的请求被阻塞，但是CPU的资源使用却很低，大部分
 *      线程都浪费在处理其它事件上了。所以，这就导致服务器并发量不高。
 *      而异步可以解决这个问题。
 *      我们可以把需要用到cpu的业务处理使用异步来实现，这样其他的请求就不会被阻塞，而且CPU会保持比较高的使用率。
 *
 *      情景：A是处理业务的一个步骤，A需要解决一个问题，这时候A可以去问B，让B来告诉A答案，这期间，A可以继续做自己的事情，而不需要
 *          因为B在计算而阻塞。于是，我们想到给B设置一个线程，让B去处理耗时的操作，然后处理完之后把结果告诉A。
 *       所以问题的要点在于B处理完之后如何把结果告诉A？
 *       我们可以直接在A中写一个方法对B处理完的结果进行处理，然后B处理完之后调用A这个方法。
 *       这样A调用B去处理结果，B调用A的C方法去处理结果的过程叫做回调
 *       https://www.cnblogs.com/liumaowu/p/9317240.html
 *
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/19   11:16
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CallbackTest2 {
    public static void main(String[] args) {
        B b = new B();
        A a = new A(b);
        a.ask("1 + 1 = ?");
    }
}