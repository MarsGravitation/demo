package com.microwu.cxd.annotation;

import java.lang.annotation.*;

/**
 * Description: HTTPMethod 注解
 *
 * 该注解属于运行时的 METHOD 注解，作用在一个方法上
 *
 * 用途：标识该方法为三方渠道的某个特定的文档接口，可以配置接口路径、请求方式、重试次数等信息
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   10:35
 * Update History:
 * Author        Time            Content
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HTTPMethod {

    // 请求路径
    String url();

    HTTPRequestType requestType() default HTTPRequestType.POST;

    // 重试次数
    int retryCount() default 0;

    // GET, POST 请求
    enum HTTPRequestType {
        GET,
        POST
    }

}
