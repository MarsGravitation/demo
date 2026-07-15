package com.microwu.cxd.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   17:16
 * Update History:
 * Author        Time            Content
 */
@AiService(chatMemoryProvider = "chatMemoryProvider", tools = "appointmentTools")
public interface AiDemoAssistant {

    @SystemMessage(fromResource = "zhaozhi-prompt-template.txt")
    String chat(@MemoryId Object memoryId, @UserMessage String userMessage);

}
