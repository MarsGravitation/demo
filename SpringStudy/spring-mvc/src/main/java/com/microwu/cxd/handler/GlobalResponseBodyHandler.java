package com.microwu.cxd.handler;

import com.microwu.cxd.domain.CommonResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Description:
 * 通过实现ResponseBodyAdvice 接口, 并添加@ControllerAdvice
 * <p>
 * 注意: 这里我们添加了@ControllerAdvice, 并设置了basePackages,
 * 因为实际情况中有可能引入Swagger, 这里避免拦截这些Controller
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/17   11:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ControllerAdvice(basePackages = "com.microwu.cxd.controller")
public class GlobalResponseBodyHandler implements ResponseBodyAdvice {

    /**
     * 这里表示拦截 Controller 所有方法
     *
     * @param methodParameter
     * @param aClass
     * @return boolean
     * @author chengxudong               chengxudong@microwu.com
     * @date 2019/12/17  11:40
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    /**
     * 1. 如果是CommonResult 类型, 无需包装
     * 2. 既然被GlobalResponseBodyHandler 拦截到, 就是成功
     * 失败的将会被全局异常拦截器拦截到
     *
     * @return java.lang.Object
     * @author chengxudong               chengxudong@microwu.com
     * @date 2019/12/17  11:41
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // 如果已经是 CommonResult 类型，则直接返回
        if (body instanceof CommonResult) {
            return body;
        }

        // String 有些特殊, 这里先这样处理
        if (body instanceof String) {
            return body;
        }

        // 如果不是，则包装成 CommonResult 类型
        return CommonResult.success(body);
    }
}