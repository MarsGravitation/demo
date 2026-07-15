package com.microwu.cxd.newl.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * 消费重试
 *
 * Spring-Kafka 提供消费重试的机制。在消息消费失败的时候，Spring-Kafka 会
 * 通过消费重试机制，重新投递该消息给 Consumer，让 Consumer 有机会重新消费
 * 消息，实现消费成功。
 *
 * 当然，Spring-Kafka 并不会无限重新投递消息给 Consumer 重新消费，而是在默认
 * 情况下，达到 N 次重试次数时，Consumer 还是消费失败时，该消息会进入倒死信队列。
 *
 * 死信队列用于处理无法被正常消费的消息。当一条消息初次消费失败，Spring-Kafka
 * 会自欧东进行消息重试；达到最大重试次数后，若消息依然失败，则表明消费者在正常
 * 情况下无法正确地消费该消息，此时，Spring-Kafka 不会立刻将该消息丢弃，而是
 * 将其发送到该消费者对应的特殊队列中。
 *
 * Spring-Kafka 将这种正常情况下无法被消费额消息成为死信消息 - Dead-Letter
 * Message，将存储死信消息的特殊队列成为死信队列 - Dead-Letter Queue。后续，
 * 我们可以通过对死信队列中的消息进行重发，来使得消费者实例再次进行消费。
 *
 * Spring-Kafka 的消费重试功能，通过实现自定义的 SeekToCurrentErrorHandler，
 * 在 Consumer 消费消息异常的时候，进行拦截处理：
 *  - 在重试小于最大次数时，重新投递该消息给 Consumer，让 Consumer 有机会重新
 *  消费消息，实现消费成功。
 *  - 在重试达到最大次数时，Consumer 还是消费失败时，该消息就回发送消息到死信队列。
 *  对应的死信队列的 Topic 是 xxx.DLT
 *
 * 简单说说消费重试功能的：
 *  - 在消息消费失败时，ErrorHandler 会将调用 Kafka Consumer 的 seek 方法，
 *  将 Consumer 对于该消息对应的 TopicPartition 分区的本地进度设置成该消息的
 *  位置。这样，Consumer 在下次从 Kafka Broker 拉取消息的时候，又能重新拉取到
 *  这条消费失败的消息，并且是第一条。
 *  - 同时，Spring-Kafka 使用 FailedRecordTracker 对每个 Topic 的每个
 *  TopicPartition 消费失败次数进行计数，这样相当于对该 TopicPartition 消费
 *  失败次数进行计数，这样相当于对该 TopicPartition 的第一条消费失败的消息失败
 *  次数进行计数。
 *  - 我这个版本没有设置笑一次重新消费的时间
 *  - FailedRecordTracker 提供的计数是客户端级别的，重启 JVM 应用后，计数是
 *  是会丢失的。所以，如果想要计数进行持久化，需要自己重新实现下 FailedRecordTracker，
 *  通过 Zookeeper 存储计数。
 *
 * SeekToCurrentErrorHandler 是只针对消息的单挑消费失败的消费重试处理。如果
 * 要想有消息的批量消费重试处理，可以使用 SeekToCurrentBatchErrorHandler.
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/20   17:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demo04Message {

    public static final String TOPIC = "DEMO_04";

    private Integer id;

}
