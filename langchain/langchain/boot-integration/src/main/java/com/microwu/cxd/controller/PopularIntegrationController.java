package com.microwu.cxd.controller;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: SprintBoot 整合低阶 API
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/29   17:38
 * Update History:
 * Author        Time            Content
 */
@RestController
public class PopularIntegrationController
{
    @Resource
    private ChatModel chatModel;

    // http://localhost:9003/lc4j/boot/chat
    @GetMapping(value = "/lc4j/boot/chat")
    public String chat(@RequestParam(value = "prompt", defaultValue = "你是谁") String prompt)
    {
        return chatModel.chat(prompt);
    }
}