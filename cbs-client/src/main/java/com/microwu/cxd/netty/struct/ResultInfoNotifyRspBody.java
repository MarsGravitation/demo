package com.microwu.cxd.netty.struct;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * Description: 等待人工审核结果通知Maap服务接口
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   16:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class ResultInfoNotifyRspBody extends Body {

    /**
     * 消息的唯一标识号
     */
    private String msgId;

    /**
     * 链路信息标识
     */
    private String jsonParam;

    /**
     * 0 - 放行
     * 1 - 删除
     */
    private byte operateType;

    @Override
    public void writeBody(ByteBuf byteBuf) {

    }

    /**
     * 解码
     *  这里使用工具类解析比较麻烦，直接手动解析一下
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  10:11
     *
     * @param   	byteBuf
     * @return  void
     */
    @Override
    @SuppressWarnings("Duplicates")
    public void readBody(ByteBuf byteBuf) {
        String msgId = super.readNullTerminatedString(byteBuf);
        this.msgId = msgId;

        String jsonParam = super.readNullTerminatedString(byteBuf);
        this.jsonParam = jsonParam;

        byte operateType = byteBuf.readByte();
        this.operateType = operateType;

    }
}