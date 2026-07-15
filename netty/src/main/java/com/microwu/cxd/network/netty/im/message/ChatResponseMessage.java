package com.microwu.cxd.network.netty.im.message;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   15:40
 * Update History:
 * Author        Time            Content
 */
public class ChatResponseMessage extends AbstractResponseMessage {

    private String from;
    private String content;

    public ChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public ChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return "ChatResponseMessage{" +
                    "from='" + from + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        } else {
            return super.toString();
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}
