package com.microwu.cxd.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Description:
 *
 * enable.auto.commit = false: 使用每个 ConsumerRecord 随附的偏移量保存位置
 * seek: 重新启动时，使用该方法恢复使用者的位置
 * isolation.level = read_committed: 事务相关
 *
 *  多线程处理：Kafka 使用者不是线程安全的
 *  线程模型：
 *      - 每个线程一个消费者
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/8   10:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class OfficialConsumer {

    private static final KafkaConsumer<String, String> CONSUMER;

    static {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.133.134:9092");
        props.setProperty("group.id", "test");
        // 偏移量将以 auto.commit.interval.ms 自动提交
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        CONSUMER = new KafkaConsumer<>(props);
        // 自动分区
        CONSUMER.subscribe(Arrays.asList("foo", "bar"));

        // 指定分区
//        String topic = "foo";
//        TopicPartition partition0 = new TopicPartition(topic, 0);
//        TopicPartition partition1 = new TopicPartition(topic, 1);
//        consumer.assign(Arrays.asList(partition0, partition1));
    }

    public static void main(String[] args) {
        while (true) {
            ConsumerRecords<String, String> records = CONSUMER.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }

}