package com.microwu.cxd.domain;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/17   13:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public enum ServiceExceptionEnum {

    SUCCESS(1, "success"),
    SYS_ERROR(2001001000, "服务器发生异常"),
    MISSING_REQUESST_PARAM_ERROR(2001001001, "参数缺失");

    // =========== 用户模块 ===========
    // =========== 订单模块 ===========
    // =========== 商品模块 ===========

    private Integer code;

    private String message;

    ServiceExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}