package com.microwu.cxd.netty.struct;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/27   16:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class RichcardReqBody extends Body {

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
     * 默认 0
     */
    private int tocount;

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
     * Json形式参数，可为空，可根据业务扩展需要定义
     */
    private String jsonParams;

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
     * 消息内容
     *  文本内容使用 UTF-8 编码
     *  文本长度不大于 1M 的文本
     *  转短信的 smbodytex 不需要
     */
    private String content;

    /**
     * 消息内容的HASH值编码，该字段定义填写值
     * 说明：回落文本up1.0的 SHA-256值
     */
    private String hashUp10;

    /**
     * 1)text/plain 普通文本(包含地位位置消息)。5G消息系统中默认Chatbot下发位置信息采用Geolocation fallback SMS方式。
     * 2)application/vnd.gsma.rcs-ft-http+xml，普通文件消息，可以是图片、音频、视频消息。
     *
     * 默认空串，回落up1.0内容类型
     */
    private String contentTypeUp10;

    /**
     * 消息内容长度 ，默认0，回落up1.0内容长度
     */
    private int contentLengthUp10;

    /**
     * 消息内容, 作为消息的最后一个字段，默认空串，回落up1.0内容。
     * 文本内容使用UTF-8编码格式
     * 文本长度不大于1M的文本
     * 转短信的smbodytex不需要
     */
    private String contentUp10;

    @Override
    @SuppressWarnings("Duplicates")
    public void writeBody(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.platFrom.getBytes());
        byteBuf.writeBytes(this.msgId.getBytes());
        byteBuf.writeBytes(this.from.getBytes());

        byteBuf.writeInt(this.tocount);
        byteBuf.writeByte(this.sendType);

        byteBuf.writeBytes(this.date.getBytes());
        byteBuf.writeBytes(this.jsonParams.getBytes());
        byteBuf.writeBytes(this.hash.getBytes());
        byteBuf.writeBytes(this.contentType.getBytes());

        byteBuf.writeInt(this.contentLength);

        byteBuf.writeBytes(this.content.getBytes());
        byteBuf.writeBytes(this.hashUp10.getBytes());
        byteBuf.writeBytes(this.contentTypeUp10.getBytes());

        byteBuf.writeInt(contentLengthUp10);

        byteBuf.writeBytes(this.contentUp10.getBytes());
    }

    @Override
    public void readBody(ByteBuf byteBuf) {

    }
}