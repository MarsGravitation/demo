package com.microwu.cxd.nio.http;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/20   14:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public enum StatusCodeEnum {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    StatusCodeEnum() {

    }

    StatusCodeEnum(int code, String phrase) {
        this.code = code;
        this.phrase = phrase;
    }

    private int code;

    private String phrase;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public static String queryPhrase(int code) {
        for(StatusCodeEnum statusCodeEnum : StatusCodeEnum.values()) {
            if(statusCodeEnum.getCode() == code) {
                return statusCodeEnum.getPhrase();
            }
        }
        return null;
    }
}