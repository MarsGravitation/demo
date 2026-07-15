package com.microwu.cxd.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/30   16:55
 * Update History:
 * Author        Time            Content
 */
public interface Assistant1 {
    String chat(String message);
}