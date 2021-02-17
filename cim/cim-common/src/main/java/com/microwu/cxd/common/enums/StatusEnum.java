package com.microwu.cxd.common.enums;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   14:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public enum StatusEnum {

    SUCCESS("9000", "success"),

    SERVER_NOT_AVAILABLE("7100", "cim server is not available, please try again later!");

    private final String code;

    private final String message;

    StatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}