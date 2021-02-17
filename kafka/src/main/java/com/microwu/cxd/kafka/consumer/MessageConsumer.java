package com.microwu.cxd.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/1   17:52
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MessageConsumer {
    
    private static final String TOPIC = "quickstart-events";
    private static final String BROKER_LIST = "192.168.133.134:9092";
    private static KafkaConsumer<String, String> kafkaConsumer;
    
    static {
        Properties properties = initConfig();
        kafkaConsumer = new KafkaConsumer<String, String>(properties);
        kafkaConsumer.subscribe(Arrays.asList(TOPIC));
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "test");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        try {
            while (true) {
                // 参数为 100，如果超过 100 毫秒，则不再等待
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                records.forEach(System.out::println);
//                kafkaConsumer.commitAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }
    }

}