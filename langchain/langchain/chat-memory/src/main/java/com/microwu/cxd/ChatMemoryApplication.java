package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *
 * 记忆：保存一些信息，这些信息呈现给 LLM，使其表现得好像记住了对话。
 *
 * 淘汰策略：
 *  MessageWindowChatMemory：滑动窗口，保留最近的 N 条消息
 *  TokenWindowChatMemory: 滑动窗口，保留最近 N 个令牌
 *
 * 持久化：
 *  默认情况下，ChatMemory 实现在内存中存储 ChatMessage
 *
 *  ChatMemory chatMemory = MessageWindowChatMemory.builder()
 *         .id("12345")
 *         .maxMessages(10)
 *         .chatMemoryStore(new PersistentChatMemoryStore())
 *         .build();
 *
 *  每当向 ChatMemory 添加新的 ChatMessage 时，都会调用 updateMessages 方法。这通常
 *  在与LLM的每次交互中发生两次：一次是添加新的 UserMessage，另一次是添加新的AiMessage 时。
 *  updateMessage 方法会更新与给定内存 ID 关联的所有消息。
 *  ChatMessage 可以单独存储（每条消息存一行）或者一起存储（所有的消息存一行）
 *
 *  当 ChatMemory 的用户请求所有消息时，会调用 getMessages 方法。这通常在与LLM的每次交互中
 *  发生一次。memoryId 用来区分多个用户或多个对话。
 *
 *  SystemMessage：
 *      一旦添加，SystemMessage 总是被保留
 *      一次只能保存一条 SystemMessage
 *      添加相同忽略，添加不同更新
 *
 *  AI服务使用聊天记忆来记住之前的交互
 *
 *  // 不行，所有的用户都使用相同的 ChatMemory
 *  Assistant assistant = AiServices.builder(Assistant.class)
 *     .chatLanguageModel(model)
 *     .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
 *     .build();
 *
 *  // 使用 ChatMemoryProvider
 *  interface Assistant  {
 *     String chat(@MemoryId int memoryId, @UserMessage String message);
 * }
 *
 * Assistant assistant = AiServices.builder(Assistant.class)
 *     .chatLanguageModel(model)
 *     .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
 *     .build();
 *
 * String answerToKlaus = assistant.chat(1, "Hello, my name is Klaus");
 * String answerToFrancine = assistant.chat(2, "Hello, my name is Francine");
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/30   16:35
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class ChatMemoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMemoryApplication.class, args);
    }

}
