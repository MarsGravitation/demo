package com.microwu.cxd.controller.exception;

import com.microwu.cxd.domain.CommonResult;
import com.microwu.cxd.domain.ServiceException;
import com.microwu.cxd.domain.ServiceExceptionEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/12   9:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController("/exception")
public class ExceptionController {

    @GetMapping("/error")
    public CommonResult exception() {
        throw new ServiceException(ServiceExceptionEnum.SYS_ERROR);
    }

}