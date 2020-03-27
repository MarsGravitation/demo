package com.microwu.cxd.handler;

import com.microwu.cxd.domain.CommonResult;
import com.microwu.cxd.domain.ServiceException;
import com.microwu.cxd.domain.ServiceExceptionEnum;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:
 *  1. 在类上添加@ControllerAdvice
 *  2. 根据
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/17   13:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ControllerAdvice(basePackages = "com.microwu.cxd.controller")
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult serviceExceptionHandler(ServiceException e) {
        return CommonResult.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult missingServletRequestParameterException(ServiceException e) {
        return CommonResult.error(ServiceExceptionEnum.MISSING_REQUESST_PARAM_ERROR.getCode(), ServiceExceptionEnum.MISSING_REQUESST_PARAM_ERROR.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult exception(Exception e) {
        System.out.println(e);
        return CommonResult.error(ServiceExceptionEnum.SYS_ERROR.getCode(), ServiceExceptionEnum.SYS_ERROR.getMessage());
    }

}