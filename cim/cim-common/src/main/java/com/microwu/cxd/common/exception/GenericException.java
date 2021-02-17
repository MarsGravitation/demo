package com.microwu.cxd.common.exception;

import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   13:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class GenericException extends RuntimeException {

    String code;

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String code, String message) {
        super(message);
        this.code = code;
    }

}