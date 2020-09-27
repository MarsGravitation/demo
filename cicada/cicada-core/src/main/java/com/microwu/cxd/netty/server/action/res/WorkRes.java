package com.microwu.cxd.netty.server.action.res;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WorkRes<T> {

    private String code;

    private String message;

    private T dataBody;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDataBody() {
        return dataBody;
    }

    public void setDataBody(T dataBody) {
        this.dataBody = dataBody;
    }
}