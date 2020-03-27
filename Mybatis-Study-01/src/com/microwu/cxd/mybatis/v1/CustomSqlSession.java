package com.microwu.cxd.mybatis.v1;

/**
 * Description: 自定义SqlSession
 * Author:   Administration
 * Date:     2019/2/26 16:45
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CustomSqlSession {
    // 关键的两个对象
    private CustomConfiguration configuration;
    private CustomExecutor executor;

    /**
     * @Descrip 使用构造器将两个对象构成关系
     * @author 成旭东
     * @date 2019/2/26 16:47
     * @param  * @param configuration
     * @param executor
     * @return
     */
    public CustomSqlSession(CustomConfiguration configuration, CustomExecutor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    public CustomConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @Descrip 委托configuration获取mapper
     * @author 成旭东
     * @date 2019/2/26 16:49
     * @param  * @param clazz
     * @return T
     */
    public <T> T getMapper(Class<T> clazz){
        return configuration.getMapper(clazz, this);
    }

    public <T> T selectOne(String statement, String parameter){
        return executor.query(statement, parameter);
    }
}