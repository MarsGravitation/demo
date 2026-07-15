package com.microwu.cxd.im.message;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   10:51
 * Update History:
 * Author        Time            Content
 */
public class GroupChatResponseMessage extends AbstractResponseMessage {

    private String from;
    private String content;

    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
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
    public String toString() {
        return "GroupChatResponseMessage{" +
                "from='" + from + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
