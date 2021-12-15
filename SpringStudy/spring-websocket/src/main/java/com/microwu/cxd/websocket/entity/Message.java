package com.microwu.cxd.websocket.entity;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/5  17:06
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class Message {

    private String fromUserId;

    private String toUserId;

    private String contentText;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", contentText='" + contentText + '\'' +
                '}';
    }
}
