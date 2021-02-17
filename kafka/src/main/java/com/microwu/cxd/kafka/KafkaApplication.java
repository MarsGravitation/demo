package com.microwu.cxd.kafka;

/**
 * Hello world!
 *  1. Kafka 集群结构图
 *      a. Broker: 一个 Broker 就是 Kafka 集群中的一个实例，或者说是一个服务单元。连接到同一个 zookeeper 的多个 broker 实例组成 kafka 的集群。
 *          在若干个 broker 中会有一个 broker 是 leader，其余的 broker 为 follower。leader 在集群启动时候选举出来，负责和外部的通讯。当 leader
 *          死掉的时候，follower 会再次通过选举，选举出新的 leader，确保集群的正常工作
 *      b. Consumer Group
 *      c. Topic
 *  2. Kafka 核心概念简介
 *      分区 Partition：把一个队列划分为若干个小队列。若无分区，一个 topic 只能有一个消费者，采用分区后，可以有多个分区同时消费
 *          - 一个 partition 只能被同组的一个 consumer 消费
 *          - 同一个组的一个 consumer 可以消费多个 partition
 *          - 消费效率最高的情况是 partition 和 consumer 数量相同
 *          - consumer 数量不能大于 partition，因为第一点的限制，会有 consumer 闲置
 *          - consumer group 可以被认为是一个订阅者的集群，每个 consumer 负责自己所消费的分区
 *      副本 Replica：kafka 中正本和副本都称为 Replica，但存在 leader 和 follower。每个分区的数据都会有多分副本，保证高可用
 *      kafka 通过轮训算法保证 leader replica 是均匀分布在多个 broker 上的
 *          - Replica 均匀分配在 Broker 上，同一个 partition 的 replica 不会在同一个 Broker 上
 *          - 同一个 partition 的 replica 数量不能多余 Broker 数量。多个 replica 为了数据安全，一个 Server 存多个 replica 没有意义，一个挂掉，所有副本都挂掉
 *          - 分区的 leader replica 均匀分布在 broker 上
 *      分区平衡
 *          AR: assigned replicas，已分配的副本
 *          PR: 优先副本，AR 列表中的第一个 replica 就是优先 replica
 *          ISR: in sync replicas, 同步副本
 *      Partition 的读和写
 *          写的时候，采用 round-robin 算法，轮训
 *          读的时候，消费端维护一个 offset
 *
 *          - 每个 partition 都是有序不可变的
 *          - Kafka 可以保证 partition 的消费顺序，不能保证 topic 的小粉顺序
 *          - 无论消费与否，保留周期默认为两天
 *          - 每个 consumer 维护的唯一元数据是 offset
 *          - consumer 可以重置 offset，可以以任何顺序消费
 *
 *
 * https://blog.csdn.net/liyiming2017/category_8073291.html
 *
 * 核心组件 - 控制器
 *  1. 控制器选举
 *      a. 从 zookeeper 获取临时节点信息，如果有，已经存在 leader
 *      b. 如果没有则竞争，最先写入的称为 leader，其他的抛出异常
 *
 *  2. 故障转移
 *  3. 代理上下线
 *      a. 上线时向一个节点写数据
 *      b. 监听到变化，告诉其他代理
 *
 * 核心组件 - 协调器
 *  1. 组协调器
 *      |- 选出消费者 leader
 *  2. 消费者协调器
 *      |- leader 的消费协调器负责消费者分区分配
 *      |- 非 leader 的消费者通过消费者协调器和组协调器同步分配结果
 *  3. 消费偏移量管理
 *      |- 自动提交: commitSync 阻塞，直到偏移量提交成功，会不断重试
 *      |- 手动提交: commitAsync 异步，不会重试
 *      |- 实践：正常消费，异步提交；程序报错，finally 同步提交；再均衡回调方法，同步提交
 *  4. 日志管理器
 *      |- ConcurrentSkipListMap 二分查找
 *      |- .index 稀疏索引，查询
 *      |- .log 顺序查找
 *  5. 副本管理器
 *
 */
public class KafkaApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
