package com.microwu.cxd.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Spring工具类
 *
 * @author      Sage Ro             shengjie.luo@microwu.com
 * @date        2018/7/10  10:50
 * CopyRight    北京小悟科技有限公司    http://www.microwu.com
 *
 * Update History:
 *   Author        Time            Content
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    /**
     * applicationContext instance
     */
    private static ApplicationContext applicationContext;

    /**
     * 获取ServletRequestAttributes
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/7/10  10:51
     *
     * @return {@link ServletRequestAttributes}
     */
    public static ServletRequestAttributes servletRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    /**
     * 获取httpServletRequest
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/7/10  11:42
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest httpServletRequest() {
        return servletRequestAttributes().getRequest();
    }

    /**
     * 获取httpServletResponse
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/7/10  11:42
     *
     * @return {@link HttpServletResponse}
     */
    public static HttpServletResponse httpServletResponse() {
        return servletRequestAttributes().getResponse();
    }

    /**
     * 获取httpSession
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/7/10  14:18
     *
     * @return {@link HttpSession}
     */
    public static HttpSession httpSession() {
        return httpServletRequest().getSession();
    }

    /**
     * 通过name获取Bean
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/8/7  15:02
     *
     * @param name 对象名
     * @return Bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/8/7  15:04
     *
     * @param clazz 类
     * @param <T>   generic
     * @return Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name, class获取Bean
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/8/7  15:05
     *
     * @param name  对象名
     * @param clazz 类
     * @param <T>   generic
     * @return Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
