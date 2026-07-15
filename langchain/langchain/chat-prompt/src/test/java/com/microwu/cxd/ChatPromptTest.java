package com.microwu.cxd;

import com.microwu.cxd.service.LawAssistant;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   10:33
 * Update History:
 * Author        Time            Content
 */
public class ChatPromptTest {

    /**
     * 知识产权是指自然人、法人或其他组织依法对其创造的智力成果所享有的专有权利。它是现代法律体系中保护创新、鼓励创作、促进科技进步和文化繁荣的重要制度安排。在中国，知识产权制度以《中华人民共和国民法典》为基础，并由《著作权法》《专利法》《商标法》《反不正当竞争法》等专门法律共同构成。
     *
     * 根据我国法律实践，知识产权主要包括以下几类：
     *
     * 一、著作权（版权）
     * 著作权是指作者对其创作的文学、艺术和科学作品依法享有的人身权和财产权。人身权包括发表权、署名权、修改权和保护作品完整权；财产权包括复制、发行、出租、展览、表演、放映、广播、信息网络传播、摄制、改编、翻译、汇编等权利。作品自创作完成之日起自动获得著作权，无需登记（但登记可作为权利归属的初步证据）。
     *
     * 二、专利权
     * 专利权是国家依法授予发明创造者在一定期限内对其发明创造享有的独占实施权。我国《专利法》规定了三种专利类型：
     * 1. 发明专利：针对产品、方法或其改进提出的新技术方案，保护期为20年；
     * 2. 实用新型专利：针对产品的形状、构造或其结合提出的适于实用的新技术方案，保护期为10年；
     * 3. 外观设计专利：针对产品的整体或局部的形状、图案、色彩或其结合所作出的富有美感并适于工业应用的新设计，保护期为15年（2021年《专利法》修订后延长）。
     * 专利权需经国家知识产权局审查授权后方可获得。
     *
     * 三、商标权
     * 商标权是指商标注册人对其注册商标在核定使用的商品或服务上享有的专用权。商标可以是文字、图形、字母、数字、三维标志、颜色组合或上述要素的组合，具有识别商品或服务来源的功能。注册商标受法律保护，有效期为10年，期满可续展。未经许可在相同或类似商品上使用相同或近似商标，可能构成侵权。
     *
     * 四、商业秘密
     * 商业秘密是指不为公众所知悉、具有商业价值且经权利人采取保密措施的技术信息和经营信息（如客户名单、工艺流程、配方等）。与专利不同，商业秘密无需公开，保护期理论上可无限延续，但一旦泄露即丧失保护基础。《反不正当竞争法》对侵犯商业秘密行为作出禁止性规定。
     *
     * 五、其他知识产权
     * 还包括集成电路布图设计专有权、植物新品种权、地理标志等。例如，地理标志可作为集体商标或证明商标注册，用于标识某商品来源于特定地区，其特定质量、声誉主要由该地域自然或人文因素决定（如“五常大米”“西湖龙井”）。
     *
     * 知识产权具有专有性、地域性和时间性三大特征：
     * - 专有性：权利人有权排除他人未经许可使用其智力成果；
     * - 地域性：知识产权仅在授予国或地区范围内有效；
     * - 时间性：除商业秘密外，多数知识产权在法定期限届满后进入公有领域。
     *
     * 我国高度重视知识产权保护，已建立行政保护与司法保护并行的“双轨制”体系。权利人既可向市场监管、版权、知识产权等部门投诉，也可直接向人民法院提起民事诉讼。近年来，《民法典》明确将知识产权纳入民事权利体系，相关法律多次修订以强化保护力度，体现了国家推动创新驱动发展战略的法治保障。
     *
     * 总之，知识产权制度通过赋予创作者和创新者一定期限的排他性权利，平衡个人利益与社会公共利益，激励持续创新，是建设创新型国家和高质量发展的重要法律基石。
     *
     *
     * 抱歉，我只能回答中国法律相关的问题。
     * 抱歉，我只能回答中国法律相关的问题。
     * 抱歉，我只能回答中国法律相关的问题。
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2026/2/2 10:40
     *
     * @param:
     * @return: void
     */
    @Test
    public void test() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        LawAssistant lawAssistant = AiServices.create(LawAssistant.class, model);

        String chat = lawAssistant.chat("什么是知识产权？",2000);
        System.out.println(chat);

        String chat2 = lawAssistant.chat("什么是java？",2000);
        System.out.println(chat2);

        String chat3 = lawAssistant.chat("介绍下西瓜和芒果",2000);
        System.out.println(chat3);

        String chat4 = lawAssistant.chat("飞机发动机原理",2000);
        System.out.println(chat4);
    }

    /**
     * 牙疼虽然不属于外科急症，但作为医生助手，我可以为您提供初步的评估和建议。以下是处理牙疼的步骤：
     *
     * ### 一、初步评估
     * 1. **疼痛性质**：
     *    - 是持续性跳痛（可能提示牙髓炎）？
     *    - 是咬合时剧痛（可能为根尖周炎或牙隐裂）？
     *    - 是冷热刺激后短暂疼痛（可能为牙本质敏感或早期龋齿）？
     *
     * 2. **伴随症状**：
     *    - 面部肿胀、发热（提示感染扩散，需紧急处理）
     *    - 牙龈红肿流脓（可能为牙周脓肿）
     *    - 张口受限（警惕间隙感染）
     *
     * ### 二、家庭应急处理（临时缓解）
     * - **止痛**：可服用布洛芬（Ibuprofen）400mg 或对乙酰氨基酚（Paracetamol）500mg（按说明书剂量，注意禁忌症）
     * - **口腔清洁**：用温盐水漱口（一杯温水+半茶匙盐，每日3-4次）
     * - **避免刺激**：忌冷热酸甜及患侧咀嚼
     * - **局部麻醉**：可临时使用含苯佐卡因的牙痛凝胶（如Orajel）
     *
     * > ⚠️ **切勿将阿司匹林直接贴在牙龈上**（可导致化学性灼伤）
     *
     * ### 三、必须立即就医的情况（24小时内就诊）
     * - 面部/眼睑/颈部肿胀
     * - 发热超过38℃
     * - 呼吸或吞咽困难
     * - 疼痛持续超过48小时且加重
     *
     * ### 四、专业处理方向（需牙医确诊）
     * | 可能病因       | 治疗方式                  |
     * |----------------|--------------------------|
     * | 龋齿/牙髓炎    | 根管治疗 + 充填          |
     * | 牙周脓肿       | 龈下刮治 + 抗生素        |
     * | 智齿冠周炎     | 冲洗上药 + 拔牙评估      |
     * | 牙隐裂         | 牙冠修复或拔除           |
     *
     * ### 重要提醒：
     * **牙疼不会自愈**！即使疼痛暂时缓解，感染可能仍在进展。建议72小时内预约牙科检查，拖延可能导致颌骨感染甚至败血症。
     *
     * 需要帮您查找附近的急诊牙科诊所吗？
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2026/2/2 10:46
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
                .build();

        String role = "外科医生";
        String question = "牙疼";

        /*String role = "财务会计";
        String question = "人民币大写";*/

        //1 构造PromptTemplate模板
        PromptTemplate template = PromptTemplate.from("你是一个{{it}}助手,{{question}}怎么办");
        //2 由PromptTemplate生成Prompt
        Prompt prompt = template.apply(Map.of("it",role,"question",question));
        //3 Prompt提示词变成UserMessage
//        prompt.toSystemMessage()
        UserMessage userMessage = prompt.toUserMessage();
        //4 调用大模型
        ChatResponse chatResponse = model.chat(userMessage);
        System.out.println(chatResponse.aiMessage().text());
    }

}
