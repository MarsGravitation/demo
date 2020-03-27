package com.microwu.cxd.domain;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/17   11:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class CommonResult<T> implements Serializable {

    public static Integer CODE_SUCCESS = 1;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public static <T> CommonResult<T> error(CommonResult<?> result) {
        return error(result.getCode(), result.getMessage());
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!CODE_SUCCESS.equals(code), "code 必须是错误的");
        CommonResult<T> tCommonResult = new CommonResult<>();
        tCommonResult.setCode(code);
        tCommonResult.setMessage(message);
        return tCommonResult;
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

}