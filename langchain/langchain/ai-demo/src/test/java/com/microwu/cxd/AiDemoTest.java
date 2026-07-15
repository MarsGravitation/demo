package com.microwu.cxd;

import com.microwu.cxd.service.AiDemoAssistant;
import com.microwu.cxd.service.Assistant;
import com.microwu.cxd.service.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   16:43
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
public class AiDemoTest {

    @Autowired
    private Assistant assistant;

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Autowired
    private AiDemoAssistant aiDemoAssistant;

    @Test
    public void test() {
//        String r1 = assistant.chat("我叫aaa");
//        System.out.println(r1);
//        String r2 = assistant.chat("我叫什么");
//        System.out.println(r2);

//        [{
//            "model" : "qwen3-max",
//                    "messages" : [ {
//                "role" : "system",
//                        "content" : "你是我的好朋友，我是aaa，我的年龄是18，请用东北话回答问题，回答问题的时候适当添加表情\r\n符号。\r\n今天是 2026-02-02。"
//            }, {
//                "role" : "user",
//                        "content" : "我是谁，我多大了"
//            } ],
//            "stream" : false
//        }]
        String r3 = assistant.chat4(1, "我是谁，我多大了", "aaa", 18);
        System.out.println(r3);
    }

    @Test
    public void test02() {
        String answer1 = separateChatAssistant.chat(1,"我是环环");
        System.out.println(answer1);
        String answer2 = separateChatAssistant.chat(1,"我是谁");
        System.out.println(answer2);
        String answer3 = separateChatAssistant.chat(2,"我是谁");
        System.out.println(answer3);
    }

    @Test
    public void test03() {
        String r1 = aiDemoAssistant.chat(1, "你好");
        System.out.println(r1);
    }

}
