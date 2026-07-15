package com.microwu.cxd.controller;

import com.microwu.cxd.service.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/29   17:46
 * Update History:
 * Author        Time            Content
 */
@RestController
public class DeclarativeAIServiceController {

    @Autowired
    Assistant assistant;

    @GetMapping(value = "/lc4j/boot/declarative")
    public String declarative(@RequestParam(value = "prompt", defaultValue = "你是谁") String prompt)
    {
        return assistant.chat(prompt);
    }

}
