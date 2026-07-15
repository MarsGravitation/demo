package com.microwu.cxd.annotation;

import java.lang.annotation.*;

/**
 * Description: HTTPController 注解
 *
 * 该注解属于运行时的 TYPE 注解，作用在一个类或者接口上
 *
 * 用途：标识该接口为某个三方渠道的 HTTP 网关接口，可以配置渠道基础信息、代理类等信息
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   10:30
 * Update History:
 * Author        Time            Content
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface HTTPController {

    /**
     * 三方渠道描述
     */
    String desc() default "";

    /*
     * 三方渠道类型
     */
    ThirdPartEnum thirdPart();

    /*
     * 请求 url
     */
    String baseUrl() default "";

    // 代理类
    Class<?> invocationHandlerClass();

    enum ThirdPartEnum {
        WeiXinPay
    }

}
