package com.micruwo.cxd.rocketmq.filter;

/**
 * Description:
 *
 * 示例：
 *  多数情况下，标签是一种简单而有用的设计，可以用来选择想要的消息。
 *  consumer.subscribe("TOPIC", "TAGA || TAGB || TAGC");
 *
 * 语法：
 *  1. 数值比较，如 >, >=, <, <=, BETWEEN, =;
 *  2. 字符比较，如 = <>, IN;
 *  3. IS NULL 或 IS NOT NULL;
 *  4. 逻辑 AND, OR, NOT;
 *
 * 使用限制
 *  只有推送消费者可以通过 SQL92 选择消息
 *  public void subscribe(final String topic, final MessageSelector messageSelector);
 *
 * 生产者示例
 *  可以在发送时通过方法将属性翻入消息中
 *  psg.putUserProperty("a", String.valueOf(i));
 *
 * 消费者示例
 *  消费时通过 MessageSelector.bySql 选择消息
 *
 *  consumer.subscribe("TopicTest", MessageSelector.bySql("a between 0 and 3"));
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/25   14:22
 * Update History:
 * Author        Time            Content
 */
public class FilterExample {
}
