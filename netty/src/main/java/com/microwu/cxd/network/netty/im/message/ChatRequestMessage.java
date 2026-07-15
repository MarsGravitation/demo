package com.microwu.cxd.network.netty.im.message;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   15:38
 * Update History:
 * Author        Time            Content
 */
public class ChatRequestMessage extends Message {

    private String content;
    private String to;
    private String from;

    public ChatRequestMessage() {

    }

    public ChatRequestMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }

    @Override
    public String toString() {
        return "ChatRequestMessage{" +
                "content='" + content + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
