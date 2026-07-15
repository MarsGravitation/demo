package com.microwu.cxd.newl.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * 消费进度的提交机制
 *
 * 原生 Kafka Consumer 消费端，有两种消费进度提交的提交机制
 *  - 默认，自动提交，通过配置 enable.auto.commit=true，每过 auto.
 *  commit.interval.ms 时间间隔，都会自动提交消费进度。而每次提交的时机，
 *  是在 Consumer 的 #Poll 方法的逻辑里完成，在每次从 Kafka Broker 拉取
 *  消息时，会检查是否到达自动提交的时间间隔，如果是，name就回提交上一次轮训
 *  拉取的位置。
 *
 *  - 手动提交，通过配置 enable.auto.commit=false，后续通过 Consumer
 *  的 #conmmitSync() 或 #commitAsync() 方法，同步或异步提交消费进度
 *
 * Spring-Kafka Consumer 消费端，提供了更丰富的消费者进度的提交机制。
 * 当然，也是分成自动提交和手动提交两个大类。在 AckMode 枚举类中
 *
 * RECORD, BATCH, TIME, COUNT, COUNT_TIME,
 *
 * MANUAL, MANUAL_IMMEDIATE
 *
 * 存在原生 Kafka 和 Spring-Kafka 提供的两种消费进度的提交机制，该怎么配置？
 *
 *  - 使用原生 Kafka 的方式，通过配置
 *  spring.kafka.consumer.enable-auto-commit=true
 *  spring.kafka.consumer.auto-commit-interval 设置自动提交的频率
 *
 *  - 使用 Spring-Kafka 的方式，通过配置
 *  spring.kafka.consumer.enable-auto-commit=false
 *  spring.kafka.listener.ack-mode // 设置具体模式
 *  spring.kafka.listener.ack-time // 自动提交的时间间隔
 *  spring.kafka.listener.ack-count // 消息条数
 *
 * 默认什么都不配置的情况下，使用 Spring-Kafka 的 BATCH 模式：每一次消息
 * 被消费完成后，在下次拉取消息之前，自动提交
 *
 *
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/24   14:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demo08Message {

    public static final String TOPIC = "DEMO_08";

    private Integer id;

}
