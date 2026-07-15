package com.microwu.cxd.controller;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/29   16:20
 * Update History:
 * Author        Time            Content
 */
@RestController
public class HelloWorldController {

    @Autowired
    private ChatModel chatModel;

    @RequestMapping("/hello")
    public String hello() {
        return chatModel.chat("你是谁？");
    }

}
