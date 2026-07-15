package com.microwu.cxd;

import com.microwu.cxd.service.Assistant;
import com.microwu.cxd.service.Assistant1;
import com.microwu.cxd.service.PersistentChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import org.junit.jupiter.api.Test;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/30   16:54
 * Update History:
 * Author        Time            Content
 */
public class ChatMemoryTest {

    /**
     * 你好，aaa！有什么我可以帮你的吗？😊
     * 你刚刚告诉我你是“aaa”，所以你是 aaa！😄
     * 如果你是在开玩笑，或者想让我记住更多关于你的信息，也可以告诉我你的名字、兴趣，或者其他你想分享的内容～
     * =============================
     * 你刚刚说你是 **bbb**，所以——
     * **你是 bbb！** ✨
     * <p>
     * 不过如果你是在玩“身份切换”小游戏，或者想看看我能不能跟上你的节奏……那我得说：你成功让我注意到了！😉
     * 随时告诉我你是谁、想聊什么，我都在～
     * 哦！原来你是 bbb 啊～ 😊
     * （看来刚才的“aaa”是测试我认不认真听吗？😉）
     * <p>
     * 很高兴认识你，bbb！有什么我可以帮你的吗？
     *
     * @author chengxudong               chengxudong@eversec.com
     * @date 2026/1/30 17:08
     * @param:
     * @return: void
     */
    @Test
    public void test01() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        Assistant1 assistant = AiServices.builder(Assistant1.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String answerToKlaus = assistant.chat("我是aaa");
        System.out.println(answerToKlaus);
        System.out.println(assistant.chat("我是谁"));
        System.out.println("=============================");
        String answerToFrancine = assistant.chat("我是bbb");
        System.out.println(assistant.chat("我是谁"));
        System.out.println(answerToFrancine);
    }

    /**
     * 你好，aaa！有什么我可以帮你的吗？😊
     * 你刚刚告诉我你是“aaa”呀！😄
     * 不过如果你是在开玩笑，或者想让我用别的名字叫你，也可以告诉我你希望我怎么称呼你～
     * =============================
     * 你是bbb呀！刚刚你告诉我的～
     * 有什么我可以帮你的吗？😄
     * 你好，bbb！有什么我可以帮你的吗？😊
     *
     * @author chengxudong               chengxudong@eversec.com
     * @date 2026/1/30 17:09
     * @param:
     * @return: void
     */
    @Test
    public void test02() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String answerToKlaus = assistant.chat(1, "我是aaa");
        System.out.println(answerToKlaus);
        System.out.println(assistant.chat(1, "我是谁"));
        System.out.println("=============================");
        String answerToFrancine = assistant.chat(2, "我是bbb");
        System.out.println(assistant.chat(2, "我是谁"));
        System.out.println(answerToFrancine);
    }

    /**
     *
     * []
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }]
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }]
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }]
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }, AiMessage { text = "你好，aaa！很高兴认识你 😊
     * 有什么我可以帮你的吗？或者你想聊些什么呢？" toolExecutionRequests = [] }]
     * 你好，aaa！很高兴认识你 😊
     * 有什么我可以帮你的吗？或者你想聊些什么呢？
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }, AiMessage { text = "你好，aaa！很高兴认识你 😊
     * 有什么我可以帮你的吗？或者你想聊些什么呢？" toolExecutionRequests = [] }]
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }, AiMessage { text = "你好，aaa！很高兴认识你 😊
     * 有什么我可以帮你的吗？或者你想聊些什么呢？" toolExecutionRequests = [] }, UserMessage { name = null contents = [TextContent { text = "我叫什么" }] }]
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }, AiMessage { text = "你好，aaa！很高兴认识你 😊
     * 有什么我可以帮你的吗？或者你想聊些什么呢？" toolExecutionRequests = [] }, UserMessage { name = null contents = [TextContent { text = "我叫什么" }] }]
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }, AiMessage { text = "你好，aaa！很高兴认识你 😊
     * 有什么我可以帮你的吗？或者你想聊些什么呢？" toolExecutionRequests = [] }, UserMessage { name = null contents = [TextContent { text = "我叫什么" }] }]
     * [UserMessage { name = null contents = [TextContent { text = "你好，我叫aaa" }] }, AiMessage { text = "你好，aaa！很高兴认识你 😊
     * 有什么我可以帮你的吗？或者你想聊些什么呢？" toolExecutionRequests = [] }, UserMessage { name = null contents = [TextContent { text = "我叫什么" }] }, AiMessage { text = "你刚才说你叫 **aaa** 呀！😊
     * 所以你的名字是 aaa～
     * 如果这是昵称或者代号，也可以告诉我你希望我怎么称呼你哦！" toolExecutionRequests = [] }]
     * 你刚才说你叫 **aaa** 呀！😊
     * 所以你的名字是 aaa～
     * 如果这是昵称或者代号，也可以告诉我你希望我怎么称呼你哦！
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2026/1/30 17:18
     *
     * @param:
     * @return: void
     */
    @Test
    public void test03() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder().maxMessages(10)
                .id(memoryId).chatMemoryStore(new PersistentChatMemoryStore()).build();

        Assistant1 assistant = AiServices.builder(Assistant1.class)
                .chatModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .build();

        System.out.println(assistant.chat("你好，我叫aaa"));
        System.out.println(assistant.chat("我叫什么"));

    }

}
