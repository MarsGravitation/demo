package com.microwu.cxd.manage.constant;

import lombok.AllArgsConstructor;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   9:58
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@AllArgsConstructor
public enum  HttpResponseStatusEnum implements CommonResponse{

    SUCCESS("0", "success"),
    FORBIDDEN_OPERATION("2", "forbidden");

    private String code;

    private String message;
    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Object getResult() {
        return null;
    }
}