package com.microwu.cxd.service;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/30   16:40
 * Update History:
 * Author        Time            Content
 */
public class PersistentChatMemoryStore implements ChatMemoryStore {

    private List<ChatMessage> list = new ArrayList<>();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // 实现通过内存 ID 从持久化存储中获取所有的消息
        // 可以使用ChatMessageDeserializer.messageFromJson(String)和
        // ChatMessageDeserializer.messagesFromJson(String)辅助方法
        // 轻松地从JSON反序列化聊天消息。
        System.out.println(list);
        return list;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> list) {
        // TODO: 实现通过内存ID更新持久化存储中的所有消息。
        // 可以使用ChatMessageSerializer.messageToJson(ChatMessage)和
        // ChatMessageSerializer.messagesToJson(List<ChatMessage>)辅助方法
        // 轻松地将聊天消息序列化为JSON
        this.list = list;
        System.out.println(list);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        // TODO: 实现通过内存ID删除持久化存储中的所有消息。
        this.list.clear();
    }
}
