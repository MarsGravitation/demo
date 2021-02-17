package com.microwu.cxd.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/1   16:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MessageProducer {

    private static final String TOPIC = "quickstart-events";
    private static final String BROKER_LIST = "192.168.133.134:9092";
    private static KafkaProducer<String, String> producer;

    static {
        Properties configs = initConfig();
        producer = new KafkaProducer<>(configs);
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        // 0，不等服务器响应，直接返回发送成功
        // 1, leader 副本收到消息后返回成功
        // all, 所有参与的副本都复制完成后返回成功
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        try {
            String message = "hello world";
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);
            RecordMetadata metadata = producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        System.out.println("prefect!");
                    }
                    if (recordMetadata != null) {
                        System.out.println("offset: " + recordMetadata.offset() + " ;partition: " + recordMetadata.partition());
                    }
                }
            }).get();
            System.out.println(metadata);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

}