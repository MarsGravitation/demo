package com.microwu.cxd.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   16:46
 * Update History:
 * Author        Time            Content
 */
@AiService(chatMemoryProvider = "chatMemoryProvider")
public interface SeparateChatAssistant {

    String chat(@MemoryId int memoryId, @UserMessage String userMessage);

}
