package com.microwu.cxd.newl.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * 并发消费
 *
 * 以前，我们配置的每一个 Spring-Kafka @KafkaListener，都是串行消费的。
 * 显然，在这监听的 Topic 美妙消费量比较大的时候，会导致消费不及时，导致消息
 * 挤压的问题。
 *
 * concurrency 可以指定并发消费的线程数
 *
 * concurrency = 2: 创建 2 个 Kafka Consumer。注意：是 2 个 Kafka
 * Consumer！！！后续，每个 Kafka Consumer 会被单独分配到一个线程中，
 * 进行拉取消息，消费消息。
 *
 * 之后，Kafka Broker 会将 Topic 分配给创建的 2 个 Kafka Consumer
 * 各 5 个 Partition。
 *
 * 这样，创建 2 个 Kafka Consumer，就在各自的县城中，拉取各自的 Topic
 * 的 Partition 的消息，各自串行消费。从而，实现多线程的并发消费。
 *
 * 不过有一点要注意，不要配置 concurrency 属性过大，则创建的 Kafka Consumer
 * 分配不到消费 Topic 的 Partition 分区，导致不断的空轮训。
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/24   8:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demo06Message {

    public static final String TOPIC = "DEMO_06";

    private Integer id;

}
