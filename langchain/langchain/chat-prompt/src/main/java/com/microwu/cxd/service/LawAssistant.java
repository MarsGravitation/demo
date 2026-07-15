package com.microwu.cxd.service;

import com.microwu.cxd.model.LawPrompt;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   10:36
 * Update History:
 * Author        Time            Content
 */
public interface LawAssistant {

    //案例1
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    @UserMessage("请回答以下法律问题：{{question}},字数控制在{{length}}以内")
    String chat(@V("question") String question, @V("length") int length);

    //案例2
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    String chat(LawPrompt lawPrompt);

}
