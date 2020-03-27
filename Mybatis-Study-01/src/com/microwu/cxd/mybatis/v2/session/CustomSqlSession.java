package com.microwu.cxd.mybatis.v2.session;

import com.microwu.cxd.mybatis.v2.executor.CustomExecutor;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Description: 自定义的SqlSession
 * Author:   Administration
 * Date:     2019/2/28 14:50
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CustomSqlSession {
    // 持有的两个关键对象
    private CustomConfiguration configuration;
    private CustomExecutor executor;

    /**
     * @Descrip 使用构造器将两个对象构成联系
     * @author 成旭东
     * @date 2019/2/28 14:54
     * @param  * @param configuration
     * @return
     */
    public CustomSqlSession(CustomConfiguration configuration){
        this.configuration = configuration;
        // 这里决定是否需要开启缓存，从configuration中判断，创建对应的Executor
        this.executor = configuration.newExecutor();
    }

    public CustomConfiguration getConfiguration(){
        return this.configuration;
    }

    /**
     * @Descrip 委托给configuration创建代理对象
     * @author 成旭东
     * @date 2019/2/28 14:58
     * @param  * @param clazz
     * @return T
     */
    public <T> T getMapper(Class<T> clazz){
        return configuration.getMapper(clazz, this);
    }

    public <T> T selectOne(String statement, String parameter, Class pojo) throws IllegalAccessException, InvocationTargetException, InstantiationException, SQLException, NoSuchMethodException, ClassNotFoundException {
        return (T) this.executor.query(statement, parameter, pojo);
    }
}