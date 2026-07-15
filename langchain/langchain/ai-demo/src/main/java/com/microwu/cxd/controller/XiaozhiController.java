package com.microwu.cxd.controller;

import com.microwu.cxd.domain.ChatMessages;
import com.microwu.cxd.service.AiDemoAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   17:35
 * Update History:
 * Author        Time            Content
 */
@RestController
public class XiaozhiController {

    @Autowired
    private AiDemoAssistant assistant;

    @RequestMapping("/chat")
    public String chat(@RequestBody ChatMessages chatMessages) {
        return assistant.chat(chatMessages.getMemoryId(), chatMessages.getMessage());
    }

}
