package com.microwu.cxd.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   16:39
 * Update History:
 * Author        Time            Content
 */
@AiService(chatMemory = "chatMemory")
public interface Assistant {

//    @SystemMessage("你是我的好朋友，请用东北话回答问题。")
//    @SystemMessage("你是我的好朋友，请用东北话回答问题。今天是{{current_date}}")
    //@SystemMessage(fromResource = "my-prompt-template.txt")

    // 你是我的好朋友，请用上海话回答问题，并且添加一些表情符号。 我叫aaa
    @UserMessage("你是我的好朋友，请用上海话回答问题，并且添加一些表情符号。 {{it}}")
    String chat(String message);

    // @V 明确指定传递的参数名称
    @UserMessage("你是我的好朋友，请用上海话回答问题，并且添加一些表情符号。{{message}}")
    String chat2(@V("message") String userMessage);

    // 两个参数，必须使用 @V
    @UserMessage("你是我的好朋友，请用粤语回答问题。{{message}}")
    String chat3(@MemoryId int memoryId, @V("message") String userMessage);

    // @SystemMessage 和 @V 一起使用
    @SystemMessage(fromResource = "my-prompt-template3.txt")
    String chat4(
            @MemoryId int memoryId,
            @UserMessage String userMessage,
            @V("username") String username,
            @V("age") int age
    );

}
