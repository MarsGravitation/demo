package com.microwu.cxd.tool.config;

import com.microwu.cxd.tool.log.PatternMappingFilterProxy;
import com.microwu.cxd.tool.log.SimpleRecorderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.Filter;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/18   10:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class CustomToolConfiguration {

    @Bean
    public FilterRegistrationBean patternMappingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        PatternMappingFilterProxy filter = new PatternMappingFilterProxy(new SimpleRecorderFilter(), "/**");
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}