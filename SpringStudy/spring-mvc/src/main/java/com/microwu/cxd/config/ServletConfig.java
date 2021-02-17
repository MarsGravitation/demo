package com.microwu.cxd.config;

import com.microwu.cxd.component.HttpBodyRecorderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/31   15:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
//@Configuration
public class ServletConfig {

    /**
     * 日志过滤器
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/21  9:42
     *
     * @param
     * @return  org.springframework.boot.web.servlet.FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean logFilterRegister() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        // 配置过滤器
        registrationBean.setFilter(new HttpBodyRecorderFilter());
        // 配置 UrlPattern
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("httpBodyRecorderFilter");
        registrationBean.setOrder(LOWEST_PRECEDENCE);
        return registrationBean;
    }

}