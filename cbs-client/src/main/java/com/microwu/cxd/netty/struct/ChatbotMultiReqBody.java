package com.microwu.cxd.netty.struct;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   17:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class ChatbotMultiReqBody extends Body {

    /**
     * 消息来源
     */
    private String platFrom;

    /**
     * 消息唯一标识号
     */
    private String msgId;

    /**
     * 消息发送方地址 - 商户 ID
     */
    private String from;

    /**
     * 发送方式
     *  8 - Maap 元素上传
     */
    private byte sendType;

    /**
     * 日期
     */
    private String date;

    /**
     * 消息内容的 HASH 值编码
     */
    private String hash;

    /**
     * 多媒体消息类型
     */
    private String contentType;

    /**
     * 消息内容长度
     */
    private int contentLength;

    /**
     * Json 形式参数
     */
    private String jsonParams;

    /**
     * 富媒体文件路径及文件名
     */
    private String fileName;

    /**
     * 文件名称 + 扩展名
     */
    private String originFileName;

    @Override
    @SuppressWarnings("Duplicates")
    public void writeBody(ByteBuf byteBuf) {

        byteBuf.writeBytes(platFrom.getBytes());
        byteBuf.writeBytes(msgId.getBytes());
        byteBuf.writeBytes(from.getBytes());

        byteBuf.writeByte(this.sendType);

        byteBuf.writeBytes(date.getBytes());
        byteBuf.writeBytes(hash.getBytes());
        byteBuf.writeBytes(contentType.getBytes());

        byteBuf.writeInt(this.contentLength);

        byteBuf.writeBytes(jsonParams.getBytes());
        byteBuf.writeBytes(fileName.getBytes());
        byteBuf.writeBytes(originFileName.getBytes());
    }

    @Override
    public void readBody(ByteBuf byteBuf) {

    }
}