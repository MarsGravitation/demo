package com.microwu.cxd.mybatis.v2.executor;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Description: 自定义Executor
 * Author:   Administration
 * Date:     2019/2/28 15:06
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class SimpleExecutor implements CustomExecutor {
    /**
     * @Descrip 这里为默认的Executor，细化了职责，工作交给Handler执行
     * @author 成旭东
     * @date 2019/2/28 15:09
     * @param  * @param statement
     * @param parameter
     * @param pojo
     * @return T
     */
    @Override
    public <T> T query(String statement, String parameter, Class pojo) throws IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        StatementHandler handler = new StatementHandler();
        return handler.query(statement, parameter, pojo);
    }
}