package com.microwu.cxd.network.netty.blog.rpc.client;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Result<T> implements Serializable {

    private T data;
    private boolean success;
    private String failCode;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailCode() {
        return failCode;
    }

    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }
}