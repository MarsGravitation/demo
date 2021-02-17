package com.microwu.cxd.queue.disruptor.ifeve;

/**
 * Description: Disruptor 流程分析
 *  1. Disruptor 为什么这么快？
 *      a. 它是一个环，首尾相接的环，ringBuffer 拥有一个序号，这个序号指向数组中下一个可用的元素
 *      b. 没有尾指针，只维护了一个指向下一个可用位置的序号。
 *          不删除 buffer 中的数据，知道新的数据覆盖
 *      c. 数组比链表快，CPU 会缓存数组中的元素；其次可以为数组预先分配内存，使得对象一直存在，不需要垃圾回收
 *
 *  2. 如何从 ringBuffer 读取？
 *      a. Consumer 从 RingBuffer 里读取数据，通过访问 ConsumerBarrier - 这个对象有 RingBuffer 创建并代表消费者与 RingBuffer 进行交互。
 *          就像 RingBuffer 需要一个序号才能找到下一个可用节点一样，消费者也需要知道它要处理的序号，比如消费者处理完了序号 8 之前的所有数据，
 *          那么它期待访问的下一个序号是 9
 *          consumerBarrier.waitFor(nextSequence);
 *          ConsumerBarrier 返回 RingBuffer 的最大可访问序号 12
 *      b. 消费者会一直等待，等待更多数据被写入 RingBuffer。并且一旦数据写入后消费者会收到通知，消费者可以让 ConsumerBarrier 去取数据，消费者更新自己的 cursor
 *          一个好处是消费者不需要去轮训判断是否可以获取数据，只需要说当数字比我这个大的时候请告诉我，而且消费者对节点的操作是读不是写，因此不用加锁
 *          另一个好处是你可以用多个消费者去读取同一个 RingBuffer，不需要加锁
 *
 *  3. 写入 RingBuffer
 *      a. ProducerBarriers：首先你的生产者需要申请 buffer 里的下一个节点，然后当生产者向节点写完数据后，他会调用 ProducerBarrier 的 commit 方法
 *      b. ProducerBarrier 如何防止 RingBuffer 重叠？
 *          ProducerBarrier 负责所有的交互细节来从 RingBuffer 中找到下一个节点，然后才允许生产者向它写入数据。
 *          ProducerBarrier 拥有所有正在访问 RingBuffer 的消费者列表，因为需要检查所有消费者读到哪里了。这里是由消费者负责通知它们读到那个序号了。
 *          假设生产则想要写入 RingBuffer 中序号 3 占据的节点，因为它是 RingBuffer 当前游标的下一个节点，但是有一个消费者停在那，所以 ProducerBarrier 停下来自旋
 *      c. 申请下一个节点
 *          假设消费者已经处理完序号 3，ProducerBarrier 会看到下一个节点可用了，他会抢占这个节点，把序号更新为 13，然后把 Entry 返回给生产者
 *      d. 提交新的数据
 *          当生产者结束向 Entry 写入数据后，他会要求 ProducerBarrier 提交。
 *          ProducerBarrier 先等待 RingBuffer 的游标追上当前位置（单生产者没意义），然后 ProducerBarrier 更新 RingBuffer 的游标到 13.接下来 ProducerBarrier 会让
 *          消费者知道 buffer 中有新东西了
 *      e. ProducerBarrier 批处理
 *          ProducerBarrier 知道 RingBuffer 的大小，也知道最慢的消费者位置，因此他能够发现当前那些节点是可用的
 *      f. 多个生产者的场景
 *          在多个生产者的场景下，你还需要其他东西来追踪序号。这个序号是值当前可写入的序号，不是 RingBuffer 的游标 + 1
 *          每个生产者都想 ClaimStrategy 申请下一个可用的节点。生产者 1 拿到 13，生产者 2 拿到 14，RingBuffer 指向 12
 *          假设因为生产者 1 没有提交数据，而生产者 2 已经提交，并且想 ProducerBarrier 发出了请求
 *          ProducerBarrier 只有在 RingBuffer 游标到达准备提交的节点的前一个节点提交它才会提交。也就是 13 不提交 14 不能提交，因此 ClaimStrategy 自旋
 *          当生产者 1 提交 13，ProducerBarrier 让ClaimStrategy 先等待 RingBuffer 的游标到达 12，现在已经到达了，然后更新到 13，让 ProducerBarrier 戳一下
 *          WaitStrategy 告诉所有人 RingBuffer 有更新了，然后 ProducerBarrier 完成生产者 2 的请求
 *          尽管生产者在不同的时间完成数据写入，但是 RingBuffer 的内容顺序总会遵循 nextEntry 的初始调用顺序
 *
 * https://ifeve.com/?x=0&y=0&s=disruptor
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/28   15:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DisruptorProcess {
}