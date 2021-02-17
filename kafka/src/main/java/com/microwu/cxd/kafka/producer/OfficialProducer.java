package com.microwu.cxd.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Description:
 *  生产者是线程安全的，跨线程共享一个生产者实例通常要比拥有多个实例快
 *
 *  生产者由一个缓冲空间池和一个 I/O 后台线程组成，该缓冲池保存尚未传输到服务器的记录，该 I/O 线程负责将这些记录转换为请求并将它们传输到集群。
 *  使用后午饭关闭生产者将泄漏这些资源。
 *
 *  send 方法是异步的。调用时，它将记录添加到暂挂记录发送和立即返回的缓冲区中。这使生产者可以将各个记录一起批处理以提高效率。
 *
 *  acks 配置控制用于确定请求完成的条件
 *  retries 重试
 *  batch.size 生产者为每个分区维护为发送记录的缓冲区
 *  linger.ms 在发送请求钱等待毫秒数，以希望会有更多的记录来填充同一批次
 *  buffer.memory 控制生产者可用于缓冲的内存总量。如果记录的发送速度超过了将记录发送到服务器的速度，则该缓冲区空间将被被耗尽。当缓冲区空间耗尽是，其他发送调用将阻塞
 *  max.block.ms 超出此时间后引发 TimeoutException
 *  key.serializer 和 value.serializer
 *
 *  幂等生产者：恰好一次，允许程序原子的将消息发送到多个分区和主题！
 *  enable.idempotence = true
 *  retries 建议保留未设置状态，即 Integer.MAX_VALUE
 *  acks = all
 *  send 即使无限次重试也返回错误，建议关闭生产者并价差最后产生的消息内容，以确保不能重复
 *  只能保证在单个绘画中发送的消息具有幂等性
 *
 *  事务性：
 *  transactional.id: 自动启用幂等性以及幂等性所依赖的生产者配置
 *  交易中包含的主题应配置为持久性
 *  replication.factor 至少为 3
 *  min.insync.replicas = 2
 *  必须使使用者配置为仅读已提交的消息
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/8   10:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class OfficialProducer {

    private static final KafkaProducer<String, String> PRODUCER;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.133.134:9092");
        props.put("acks", "all");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        props.put("transactional.id", "my-transactional-id");

        PRODUCER = new KafkaProducer<>(props);
        PRODUCER.initTransactions();
    }

    public static void main(String[] args) {
        try {
            PRODUCER.beginTransaction();
            for (int i = 0; i < 100; i++) {
                PRODUCER.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
            }
            PRODUCER.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            PRODUCER.close();
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            PRODUCER.abortTransaction();
        }

        PRODUCER.close();
    }

}