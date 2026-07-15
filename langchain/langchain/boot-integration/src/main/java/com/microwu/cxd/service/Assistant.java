package com.microwu.cxd.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * Description: 当应用程序启动时，LangChain4J 启动器将扫描类路径 并找到
 * 所有带有 @AiService 注解的接口。对于找到的每个 AI 服务，它将适用应用程序
 * 上下文中可用的所有 LangChain4J 组件创建此接口的实现，并将其注册为 bean
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/29   17:43
 * Update History:
 * Author        Time            Content
 */
@AiService
public interface Assistant {

    @SystemMessage("You are a polite assistant")
    String chat(String userMessage);

}
