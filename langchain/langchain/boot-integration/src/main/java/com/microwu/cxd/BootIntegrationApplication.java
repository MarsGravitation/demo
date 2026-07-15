package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *
 * LangChain4j 提供了两种抽象：低阶API和高阶API
 *  低阶：
 *      创建 LLMConfig，配置 OpenAiChatModel，使用 ChatModel 与大模型交互
 *  高阶：
 *      创建一个带有单个方法 chat 的接口，接受 String 作为输入
 *      interface Assistant {
 *
 *          String chat(String userMessage);
 *      }
 *
 *      创建底层低级组件
 *
 *      ChatLanguageModel model = OpenAiChatModel.builder()
 *     .apiKey(System.getenv("OPENAI_API_KEY"))
 *     .modelName(GPT_4_O_MINI)
 *     .build();
 *
 *     创建 AI 服务实例
 *     Assistant assistant = AiServices.create(Assistant.class, model);
 *
 *     原理
 *     AiServices 创建一个实现该接口的代理对象
 *
 * LangChain4j 整合 SpringBoot 也有两种：低阶和高阶
 *
 * 低阶：
 *  配置 yml -> LLMConfig，对应的就是以前手动创建 OpenAiChatModel，使用 ChatModel 与大模型交互
 * 高阶：
 *  自定义一个接口并添加注解 @AiService
 *  SpringBoot 会自动配置，等同于上面的 AiServices.create()
 *
 *  1. 依赖
 *
 * 低阶
 *  <dependency>
 *     <groupId>dev.langchain4j</groupId>
 *     <artifactId>langchain4j-open-ai-spring-boot-starter</artifactId>
 *     <version>1.0.0-beta3</version>
 * </dependency>
 *
 * 高阶
 * <dependency>
 *     <groupId>dev.langchain4j</groupId>
 *     <artifactId>langchain4j-spring-boot-starter</artifactId>
 *     <version>1.0.0-beta3</version>
 * </dependency>
 *
 * 2. 配置模型参数
 *
 * langchain4j.open-ai.chat-model.api-key=${OPENAI_API_KEY}
 * langchain4j.open-ai.chat-model.model-name=gpt-4o
 * langchain4j.open-ai.chat-model.log-requests=true
 * langchain4j.open-ai.chat-model.log-responses=true
 * ...
 *
 * 这种情况下，将自动创建 OpenAiChatModel 的实例
 *
 * 3. @AiService
 * LangChain4j 启动器将扫描类路径 并找到所有带有 @AiService 注解的接口。对于找到的AI服务，
 * 它将使用应用程序上下文中可用的所有 LangChain4j 组件创建此接口的实现，并将其注册为 bean
 *
 * 4. @Tool
 * @Component
 * public class BookingTools {
 *
 *     private final BookingService bookingService;
 *
 *     public BookingTools(BookingService bookingService) {
 *         this.bookingService = bookingService;
 *     }
 *
 *     @Tool
 *     public Booking getBookingDetails(String bookingNumber, String customerName, String customerSurname) {
 *         return bookingService.getBookingDetails(bookingNumber, customerName, customerSurname);
 *     }
 *
 *     @Tool
 *     public void cancelBooking(String bookingNumber, String customerName, String customerSurname) {
 *         bookingService.cancelBooking(bookingNumber, customerName, customerSurname);
 *     }
 * }
 *
 * 5. 显式组件装配
 * 如果有多个 AI 服务，并希望将不同的 LangChain4j 组件装配到每个服务中，可以使用显式装配模式
 * （@AiService(wiringMode = EXPLICIT)）指定要使用的组件。
 *
 * @AiService(wiringMode = EXPLICIT, chatModel = "openAiChatModel")
 * interface OpenAiAssistant {
 *
 *     @SystemMessage("You are a polite assistant")
 *     String chat(String userMessage);
 * }
 *
 * @AiService(wiringMode = EXPLICIT, chatModel = "ollamaChatModel")
 * interface OllamaAssistant {
 *
 *     @SystemMessage("You are a polite assistant")
 *     String chat(String userMessage);
 * }
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/1/29   17:32
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class BootIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootIntegrationApplication.class, args);
    }

}
