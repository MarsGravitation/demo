package com.microwu.cxd.jackson.pojo;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/25   10:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TwitterEntry {

    private long id;

    private String text;

    private int fromUserId;

    private int toUserId;

    private String languageCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "TwitterEntry{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }
}