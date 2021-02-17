package com.microwu.cxd.spring.domain;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   9:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Hello implements InitializingBean, DisposableBean {

    private String name;

    /**
     * ApplicationContext 完全初始化一个 bean 以后，包括对应的依赖项都注入后
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  10:34
     *
     * @param
     * @return  void
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(name, "name should not be null.");
    }

    /**
     * ApplicationContext 被销毁前
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  10:35
     *
     * @param
     * @return  void
     */
    @Override
    public void destroy() throws Exception {

    }

    /**
     * 在当前 bean 被完全初始化后被调用
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  10:37
     *
     * @param
     * @return  void
     */
    public void init() {
        Assert.notNull(name, "name should not be null.");
    }

    @PostConstruct
    public void construct() {
        Assert.notNull(name, "name should not be null.");
    }

    @PreDestroy
    public void preDestroy() {

    }
}