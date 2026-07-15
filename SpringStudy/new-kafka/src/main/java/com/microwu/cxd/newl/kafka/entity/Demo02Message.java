package com.microwu.cxd.newl.kafka.entity;

/**
 * Description:
 *
 * Kafka 提供的批量发送消息，它提供了一个 RecordAccumulator 消息收集器，将发送
 * 给相同 Topic 的相同 Partition 分区的消息们，偷偷收集在一起，当满足条件时，
 * 一次性批量发送提交给 Kafka Broker。下面三个条件，满足其一灰批量发送：
 *  - batch-size: 超过收集的消息数量的最大条数
 *  - buffer-memory: 超过收集的消息占用的最大内存
 *  - linger.ms: 超过收集的时间的最大等待时长，单位: 毫秒
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/23   8:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Demo02Message {
}
