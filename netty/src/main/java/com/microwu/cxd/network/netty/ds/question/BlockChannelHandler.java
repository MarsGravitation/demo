package com.microwu.cxd.network.netty.ds.question;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * Description:
 *
 * 1. Netty 的 NIO 线程常见的阻塞场景
 *  ChannelHandler 里的业务逻辑，正常来说是由 NioEventLoop(NIO) 线程
 *  串行执行，如果某个业务 handler 的流程非常耗时，比如需要查询数据库，那么
 *  为了避免 I/O 线程被长时间占用，需要使用额外的非 I/O 线程池来执行这些耗时
 *  的业务逻辑。
 *
 *  具体用法是 Netty 在添加 handler 时，在 ChannelPipeline 接口提供了
 *  一个重载的 addLast 方法，专用于为对应 handler 添加 Netty 业务线程池。
 *
 * 4. 异步的执行结果怎么回到 Netty 的 NIO 线程？
 *  如果不是 NIO 线程，那么会将该流程封装成一个新的 task 扔到 NIO 线程的
 *  MPSCQ, 排队等待被 NIO 线程处理。
 *
 * 5. JDK 的线程池拒绝策略的坑可能导致阻塞
 *
 ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
 ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
 ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
 ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
 *
 * 如果使用 CallerRunsPolicy，导致会由调用方的线程 --- NioEventLoop 线程
 * 执行业务逻辑，最终导致 NioEventLoop 线程被长时间阻塞，在服务端无法及时的
 * 读取新的请求消息。
 *
 * 要么丢弃当前任务，或者报警
 *
 * 5. 没看太懂
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484495&idx=1&sn=2931a73be8ff2207aaf9b13af4c9117f&chksm=fbc0924fccb71b594a053ab17245576aeae5915a584406ae253f09d9ff4fc3738f89328b5c22&cur_album_id=1338702322071076864&scene=189#wechat_redirect
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   16:57
 * Update History:
 * Author        Time            Content
 */
public class BlockChannelHandler {

    public static void main(String[] args) {
        final DefaultEventExecutorGroup businessGroup = new DefaultEventExecutorGroup(16);
        new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                // 在 Netty 业务线程池（Netty 实现的无所串行化线程池）里执行耗时业务逻辑
                p.addLast(businessGroup, null);
            }
        };
    }
}
