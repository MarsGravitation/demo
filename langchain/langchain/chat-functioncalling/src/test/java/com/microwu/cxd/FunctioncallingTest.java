package com.microwu.cxd;

import cn.hutool.core.collection.CollUtil;
import com.microwu.cxd.service.FunctionAssistant;
import com.microwu.cxd.service.WeatherTools;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProviderResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   10:51
 * Update History:
 * Author        Time            Content
 */
public class FunctioncallingTest {

    /**
     * 14:07:30.238 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP request:
     * - method: POST
     * - url: https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
     * - headers: [Authorization: Beare...83], [User-Agent: langchain4j-openai], [Content-Type: application/json]
     * - body: {
     *   "model" : "qwen3-max",
     *   "messages" : [ {
     *     "role" : "user",
     *     "content" : "北京的天气怎么样？"
     *   } ],
     *   "stream" : false,
     *   "tools" : [ {
     *     "type" : "function",
     *     "function" : {
     *       "name" : "getWeather",
     *       "description" : "返回给定城市的天气预报",
     *       "parameters" : {
     *         "type" : "object",
     *         "properties" : {
     *           "city" : {
     *             "type" : "string",
     *             "description" : "应返回天气预报的城市"
     *           },
     *           "temperatureUnit" : {
     *             "type" : "string",
     *             "enum" : [ "CELSIUS", "FAHRENHEIT" ]
     *           }
     *         },
     *         "required" : [ "city" ]
     *       }
     *     }
     *   } ]
     * }
     *
     * 14:07:32.055 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP response:
     * - status code: 200
     * - headers: [:status: 200], [content-length: 521], [content-type: application/json], [date: Mon, 02 Feb 2026 06:07:32 GMT], [req-arrive-time: 1770012451006], [req-cost-time: 1241], [resp-start-time: 1770012452247], [server: istio-envoy], [vary: Origin,Access-Control-Request-Method,Access-Control-Request-Headers, Accept-Encoding], [x-dashscope-call-gateway: true], [x-envoy-upstream-service-time: 1240], [x-request-id: 2c43da7c-065e-9325-8101-25b841fde4a7]
     * - body: {"choices":[{"finish_reason":"tool_calls","index":0,"message":{"content":"","role":"assistant","tool_calls":[{"function":{"arguments":"{\"city\": \"北京\", \"temperatureUnit\": \"CELSIUS\"}","name":"getWeather"},"id":"call_6b48d1a72c5e4632ae743810","index":0,"type":"function"}]}}],"created":1770012451,"id":"chatcmpl-2c43da7c-065e-9325-8101-25b841fde4a7","model":"qwen3-max","object":"chat.completion","usage":{"completion_tokens":35,"prompt_tokens":295,"prompt_tokens_details":{"cached_tokens":0},"total_tokens":330}}
     *
     * 【Step 1】LLM 响应: AiMessage { text = null toolExecutionRequests = [ToolExecutionRequest { id = "call_6b48d1a72c5e4632ae743810", name = "getWeather", arguments = "{"city": "北京", "temperatureUnit": "CELSIUS"}" }] }
     * 【Step 2】检测到工具调用请求:
     *   - 工具名: getWeather
     *   - 参数: {"city": "北京", "temperatureUnit": "CELSIUS"}
     * 14:07:32.120 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP request:
     * - method: POST
     * - url: https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
     * - headers: [Authorization: Beare...83], [User-Agent: langchain4j-openai], [Content-Type: application/json]
     * - body: {
     *   "model" : "qwen3-max",
     *   "messages" : [ {
     *     "role" : "user",
     *     "content" : "北京的天气怎么样？"
     *   }, {
     *     "role" : "assistant",
     *     "tool_calls" : [ {
     *       "id" : "call_6b48d1a72c5e4632ae743810",
     *       "type" : "function",
     *       "function" : {
     *         "name" : "getWeather",
     *         "arguments" : "{\"city\": \"北京\", \"temperatureUnit\": \"CELSIUS\"}"
     *       }
     *     } ]
     *   }, {
     *     "role" : "tool",
     *     "tool_call_id" : "call_6b48d1a72c5e4632ae743810",
     *     "content" : "北京的天气是晴"
     *   } ],
     *   "stream" : false,
     *   "tools" : [ {
     *     "type" : "function",
     *     "function" : {
     *       "name" : "getWeather",
     *       "description" : "返回给定城市的天气预报",
     *       "parameters" : {
     *         "type" : "object",
     *         "properties" : {
     *           "city" : {
     *             "type" : "string",
     *             "description" : "应返回天气预报的城市"
     *           },
     *           "temperatureUnit" : {
     *             "type" : "string",
     *             "enum" : [ "CELSIUS", "FAHRENHEIT" ]
     *           }
     *         },
     *         "required" : [ "city" ]
     *       }
     *     }
     *   } ]
     * }
     *
     * 14:07:32.967 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP response:
     * - status code: 200
     * - headers: [:status: 200], [content-length: 352], [content-type: application/json], [date: Mon, 02 Feb 2026 06:07:33 GMT], [req-arrive-time: 1770012452425], [req-cost-time: 762], [resp-start-time: 1770012453187], [server: istio-envoy], [vary: Origin,Access-Control-Request-Method,Access-Control-Request-Headers, Accept-Encoding], [x-dashscope-call-gateway: true], [x-envoy-upstream-service-time: 761], [x-request-id: eb5b04da-b826-9b29-873d-42c9cb0be56e]
     * - body: {"choices":[{"finish_reason":"stop","index":0,"message":{"content":"北京的天气是晴。","role":"assistant"}}],"created":1770012452,"id":"chatcmpl-eb5b04da-b826-9b29-873d-42c9cb0be56e","model":"qwen3-max","object":"chat.completion","usage":{"completion_tokens":6,"prompt_tokens":350,"prompt_tokens_details":{"cached_tokens":0},"total_tokens":356}}
     *
     *
     * 【Final Answer】北京的天气是晴。
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2026/2/2 14:07
     *
     * @param:
     * @return: void
     */
    @Test
    public void test() {
        // 1. 创建模型
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .logRequests(true) // 日志界别设置为debug才有效
                .logResponses(true)// 日志界别设置为debug才有效
                .build();

        // 2. 定义工具规范
        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("getWeather")
                .description("返回给定城市的天气预报")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("city", "应返回天气预报的城市")
                        .addEnumProperty("temperatureUnit", List.of("CELSIUS", "FAHRENHEIT"))
                        .required("city") // 必须明确指定必需的属性
                        .build())
                .build();

        // 3. 用户输入
        UserMessage userMessage = UserMessage.from("北京的天气怎么样？");

        // 4. 第一次调用：带工具描述，让 LLM 决定是否调用
        ChatRequest request1 = ChatRequest.builder()
                .messages(userMessage)
                .toolSpecifications(toolSpecification)
                .build();
        ChatResponse response = model.chat(request1);
        AiMessage aiMessage = response.aiMessage();
        System.out.println("【Step 1】LLM 响应: " + aiMessage);

        // 5. 检查 LLM 是否请求了工具调用
        if (aiMessage.hasToolExecutionRequests()) {
            ToolExecutionRequest request1_2 = aiMessage.toolExecutionRequests().get(0);
            System.out.println("【Step 2】检测到工具调用请求:");
            System.out.println("  - 工具名: " + request1_2.name());
            System.out.println("  - 参数: " + request1_2.arguments());

            // 手动解析参数并执行工具
            WeatherTools weatherTools = new WeatherTools();
            String toolResult = weatherTools.getWeather("北京");

//            // 7. 构造工具执行结果消息
//            ToolExecutionResultMessage toolResultMessage = ToolExecutionResultMessage.from(request, toolResult);
//
//            // 8. 第二次调用：将工具结果传回 LLM，生成最终回答
//            List<ChatMessage> conversation = List.of(userMessage, aiMessage, toolResultMessage);
//
//            ChatResponse response2 = model.chat(conversation);
//            String finalAnswer = response2.aiMessage().text();


//            String toolResult = "预计明天伦敦会下雨。";
            ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(request1_2, toolResult);
            ChatRequest request2 = ChatRequest.builder()
                    .messages(List.of(userMessage, aiMessage, toolExecutionResultMessage))
                    .toolSpecifications(toolSpecification)
                    .build();
            ChatResponse response2 = model.chat(request2);

            System.out.println("\n【Final Answer】" + response2.aiMessage().text());
        }
    }

    /**
     * 14:13:56.530 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP request:
     * - method: POST
     * - url: https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
     * - headers: [Authorization: Beare...83], [User-Agent: langchain4j-openai], [Content-Type: application/json]
     * - body: {
     *   "model" : "qwen3-max",
     *   "messages" : [ {
     *     "role" : "user",
     *     "content" : "明天北京的天气会怎样?"
     *   } ],
     *   "stream" : false,
     *   "tools" : [ {
     *     "type" : "function",
     *     "function" : {
     *       "name" : "getWeather",
     *       "description" : "返回给定城市的天气预报",
     *       "parameters" : {
     *         "type" : "object",
     *         "properties" : {
     *           "city" : {
     *             "type" : "string",
     *             "description" : "应返回天气预报的城市"
     *           }
     *         },
     *         "required" : [ "city" ]
     *       }
     *     }
     *   } ]
     * }
     *
     * 14:13:58.380 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP response:
     * - status code: 200
     * - headers: [:status: 200], [content-length: 487], [content-type: application/json], [date: Mon, 02 Feb 2026 06:13:58 GMT], [req-arrive-time: 1770012837304], [req-cost-time: 1271], [resp-start-time: 1770012838575], [server: istio-envoy], [vary: Origin,Access-Control-Request-Method,Access-Control-Request-Headers, Accept-Encoding], [x-dashscope-call-gateway: true], [x-envoy-upstream-service-time: 1255], [x-request-id: f61a4953-9f90-98fc-8d79-6c0cc09f3eda]
     * - body: {"choices":[{"finish_reason":"tool_calls","index":0,"message":{"content":"","role":"assistant","tool_calls":[{"function":{"arguments":"{\"city\": \"北京\"}","name":"getWeather"},"id":"call_b06f6c36ef1f4003be7f3c5a","index":0,"type":"function"}]}}],"created":1770012837,"id":"chatcmpl-f61a4953-9f90-98fc-8d79-6c0cc09f3eda","model":"qwen3-max","object":"chat.completion","usage":{"completion_tokens":21,"prompt_tokens":271,"prompt_tokens_details":{"cached_tokens":0},"total_tokens":292}}
     *
     * 14:13:58.549 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP request:
     * - method: POST
     * - url: https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
     * - headers: [Authorization: Beare...83], [User-Agent: langchain4j-openai], [Content-Type: application/json]
     * - body: {
     *   "model" : "qwen3-max",
     *   "messages" : [ {
     *     "role" : "user",
     *     "content" : "明天北京的天气会怎样?"
     *   }, {
     *     "role" : "assistant",
     *     "tool_calls" : [ {
     *       "id" : "call_b06f6c36ef1f4003be7f3c5a",
     *       "type" : "function",
     *       "function" : {
     *         "name" : "getWeather",
     *         "arguments" : "{\"city\": \"北京\"}"
     *       }
     *     } ]
     *   }, {
     *     "role" : "tool",
     *     "tool_call_id" : "call_b06f6c36ef1f4003be7f3c5a",
     *     "content" : "北京的天气是晴"
     *   } ],
     *   "stream" : false,
     *   "tools" : [ {
     *     "type" : "function",
     *     "function" : {
     *       "name" : "getWeather",
     *       "description" : "返回给定城市的天气预报",
     *       "parameters" : {
     *         "type" : "object",
     *         "properties" : {
     *           "city" : {
     *             "type" : "string",
     *             "description" : "应返回天气预报的城市"
     *           }
     *         },
     *         "required" : [ "city" ]
     *       }
     *     }
     *   } ]
     * }
     *
     * 14:14:01.931 [main] INFO dev.langchain4j.http.client.log.LoggingHttpClient -- HTTP response:
     * - status code: 200
     * - headers: [:status: 200], [content-length: 364], [content-type: application/json], [date: Mon, 02 Feb 2026 06:14:01 GMT], [req-arrive-time: 1770012838804], [req-cost-time: 3051], [resp-start-time: 1770012841855], [server: istio-envoy], [vary: Origin,Access-Control-Request-Method,Access-Control-Request-Headers, Accept-Encoding], [x-dashscope-call-gateway: true], [x-envoy-upstream-service-time: 3050], [x-request-id: d512a72e-5671-9f7e-a0eb-ad4c10be8c18]
     * - body: {"choices":[{"finish_reason":"stop","index":0,"message":{"content":"明天北京的天气将是晴天。","role":"assistant"}}],"created":1770012838,"id":"chatcmpl-d512a72e-5671-9f7e-a0eb-ad4c10be8c18","model":"qwen3-max","object":"chat.completion","usage":{"completion_tokens":8,"prompt_tokens":312,"prompt_tokens_details":{"cached_tokens":0},"total_tokens":320}}
     *
     * 明天北京的天气将是晴天。
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2026/2/2 14:14
     *
     * @param:
     * @return: void
     */
    @Test
    public void test02() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .logRequests(true) // 日志界别设置为debug才有效
                .logResponses(true)// 日志界别设置为debug才有效
                .build();

        FunctionAssistant assistant = AiServices.builder(FunctionAssistant.class)
                .chatModel(model)
                .tools(new WeatherTools())
//                .tools(weatherTool, calculatorTool) // 👈 关键：传入多个工具
//                .toolProvider()
                .build();

//        对于每个 ToolSpecification，需要提供一个 ToolExecutor 实现， 该实现将处理 LLM 生成的工具执行请求：
//        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> {
//            Map<String, Object> arguments = fromJson(toolExecutionRequest.arguments());
//            String bookingNumber = arguments.get("bookingNumber").toString();
//            Booking booking = getBooking(bookingNumber);
//            return booking.toString();
//        };

//        一旦我们有了一个或多个（ToolSpecification，ToolExecutor）对， 我们可以在创建 AI 服务时指定它们：
//        Assistant assistant = AiServices.builder(Assistant.class)
//                .chatLanguageModel(chatLanguageModel)
//                .tools(Map.of(toolSpecification, toolExecutor))
//                .build();

        // 使用 AI 服务时，也可以为每次调用动态指定工具。 可以配置一个 ToolProvider，它将在每次调用 AI 服务时被调用， 并提供应包含在当前 LLM 请求中的工具。
        // ToolProvider 接受一个包含 UserMessage 和聊天记忆 ID 的 ToolProviderRequest， 并返回一个包含工具的 ToolProviderResult，形式为从 ToolSpecification 到 ToolExecutor 的 Map。
//        ToolProvider toolProvider = (toolProviderRequest) -> {
//            if (toolProviderRequest.userMessage().singleText().contains("booking")) {
//                ToolSpecification toolSpecification = ToolSpecification.builder()
//                        .name("get_booking_details")
//                        .description("返回预订详情")
//                        .parameters(JsonObjectSchema.builder()
//                                .addStringProperty("bookingNumber")
//                                .build())
//                        .build();
//                return ToolProviderResult.builder()
//                        .add(toolSpecification, toolExecutor)
//                        .build();
//            } else {
//                return null;
//            }
//        };
//
//        Assistant assistant = AiServices.builder(Assistant.class)
//                .chatLanguageModel(model)
//                .toolProvider(toolProvider)
//                .build();

        String chat = assistant.chat("明天北京的天气会怎样?");
        System.out.println(chat);
    }

}
