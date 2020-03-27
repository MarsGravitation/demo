package com.microwu.cxd.nio.http;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/20   15:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Response {
    private int code;

    private String version;

    private String contentType;

    private String server;

    @Override
    public String toString() {

        return "Response{}";
    }
}