package com.microwu.cxd.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * 大部分模型兼容 OpenAI
 * 也可以使用 DashScope 的方式访问通义千问 API
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/29   16:14
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class LLMConfig {

    @Bean
    public ChatModel chatModelQwen() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
        return model;
    }

}
