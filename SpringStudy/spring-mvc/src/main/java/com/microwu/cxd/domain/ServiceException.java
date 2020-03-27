package com.microwu.cxd.domain;

/**
 * Description: 对于异常的处理有两种:
 *  1. 统一封装成CommonResult 返回
 *  2. 封装成ServiceException, 然后throw
 *
 *  这里选择 throw 异常
 *      1. @Transaction 是基于异常回滚的
 *      2. 当调用别人的方法时, 如果是CommonResult 还需要判断
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/17   13:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ServiceException extends RuntimeException {

    private final Integer code;

    public ServiceException(ServiceExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());

        this.code = exceptionEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}