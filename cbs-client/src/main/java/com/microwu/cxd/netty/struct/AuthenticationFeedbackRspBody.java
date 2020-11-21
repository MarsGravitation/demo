package com.microwu.cxd.netty.struct;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * Description: 鉴权反馈响应体，这里包括 Chatbot 上传富媒体元素鉴权反馈接口 和
 *          下行富媒体卡片消息数据鉴权反馈接口
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   14:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class AuthenticationFeedbackRspBody extends Body {

    /**
     * 消息的唯一标志号
     */
    private String msgId;

    /**
     * 状态
     */
    private int distributionStatus;

    /**
     * 请求报里面的内容
     */
    private String jsonParam;

    @Override
    public void writeBody(ByteBuf byteBuf) {

    }

    @Override
    @SuppressWarnings("Duplicates")
    public void readBody(ByteBuf byteBuf) {
        String msgId = super.readNullTerminatedString(byteBuf);
        this.msgId = msgId;

        int distributionStatus = byteBuf.readInt();
        this.distributionStatus = distributionStatus;

        String jsonParam = super.readNullTerminatedString(byteBuf);
        this.jsonParam = jsonParam;
    }
}