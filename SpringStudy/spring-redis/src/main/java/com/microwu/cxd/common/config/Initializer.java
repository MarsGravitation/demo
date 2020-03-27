package com.microwu.cxd.common.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * Description:     我们的Spring配置创建了一个名为SpringSessionRepositoryFilter的SpringBean,
 *                  它实现了过滤器. SpringSessionRepositoryFilter bean负责用Spring会话支持的
 *                  自定义实现替换HttpSession. 为了让过滤器发挥它的魔力, Spring需要加载我们的Config类.
 *                  最后我们确保servlet容器对每个请求都使用我们的SpringSessionRepositoryFilter.
 *
 *                  1. 实现AbstractHttpSessionApplicationInitializer, 确保SpringBean以SpringSessionRepositoryFilter
 *                  的名称为每个请求在Servlet容器中组册.
 *                  2. AbstractHttpSessionApplicationInitializer还提供了一种机制来确保Spring加载我们的Config
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/1   13:40
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Initializer extends AbstractHttpSessionApplicationInitializer {
    public Initializer() {
        super(RedisConfig.class);
    }
}