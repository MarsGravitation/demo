package com.microwu.cxd.spring.bean;

/**
 * Description: bean 生命周期回调方法，在 ApplicationContext 初始化前和销毁之前
 *  1. 实现特定接口
 *      Spring 提供的接口 InitializingBean 和 DisposableBean
 *  2. XML 配置中配置 bean 元素的 init-method 和 destroy-method
 *      |- 注解上一般在 @Configuration 的 @Bean 可以设置
 *  3. JSR-250 注解
 *  4. 混合使用三种方式
 *      |- JSR-250, InitializingBean, XML 配置
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   10:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BeanLifeCycle {
}