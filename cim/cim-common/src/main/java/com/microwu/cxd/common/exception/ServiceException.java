package com.microwu.cxd.common.exception;

import com.microwu.cxd.common.enums.StatusEnum;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   13:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ServiceException extends GenericException {

    public ServiceException(String code, String errorMessage) {
        super(code, errorMessage);
    }

    public ServiceException(StatusEnum statusEnum) {
        super(statusEnum.getCode(), statusEnum.getMessage());
    }

}