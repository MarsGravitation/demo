package com.microwu.cxd.netty.enumx;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Description: 命令标识符取值枚举
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   11:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public enum CommandIDEnum {

    ENQUIRE_LINK_REQ(0x00000001, "EnquireLinkReq", null, "链路检测请求"),
    ENQUIRE_LINK_REP(0x80000001, "EnquireLinkRsp", null, "链路检测应答"),
    MAAP_CHATBOT_MULTI_MONITOR_REQ(0x00000004, "maap_chatbotMultiMonitorReq", "", "Maap业务Chatbot上传元素鉴权请求"),
    MAAP_CHATBOT_MULTI_MONITOR_RSP(0x80000004, "maap_chatbotMultiMonitorRsp", "com.microwu.cbs.file.common.domain.struct.AuthenticationFeedbackRspBody", "Maap业务Chatbot上传元素鉴权反馈"),
    MAAP_RICH_CARD_MONITOR_REQ(0x00000005, "maap_richcardMonitorReq", "", "Maap业务富媒体卡片消息鉴权请求"),
    MAAP_RICH_CARD_MONITOR_RSP(0x80000005, "maap_richcardMonitorRsp", "com.microwu.cbs.file.common.domain.struct.AuthenticationFeedbackRspBody", "Maap业务富媒体卡片消息鉴权反馈"),
    MAAP_RESULT_INFO_NOTIFY_REQ(0x80000007, "maap_ResultInfoNotifyReq", "com.microwu.cbs.file.common.domain.struct.ResultInfoNotifyRspBody", "等待人工审核结果通知Maap服务接口");

    /**
     * 命令标识符 - 协议号
     */
    private int parameter;

    /**
     * 名称
     */
    private String name;

    /**
     * body 对应的 class，反射生成对应的对象
     */
    private String body;

    /**
     * 描述
     */
    private String desc;

    /**
     * 存储了 协议号 - body 的对应关系
     */
    public static final Map<Integer, String> MAP = new HashMap<>();

    static {
        CommandIDEnum[] values = values();
        Stream.of(values).forEach(commandIDEnum -> {
            MAP.put(commandIDEnum.parameter, commandIDEnum.body);
        });
    }

    CommandIDEnum(int parameter, String name, String body, String desc) {
        this.parameter = parameter;
        this.name = name;
        this.body = body;
        this.desc = desc;
    }

    public int getParameter() {
        return parameter;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getDesc() {
        return desc;
    }
}