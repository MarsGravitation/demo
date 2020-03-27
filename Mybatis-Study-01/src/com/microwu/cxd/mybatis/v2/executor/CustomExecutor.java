package com.microwu.cxd.mybatis.v2.executor;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface CustomExecutor {
    public <T> T query(String statement, String parameter, Class pojo) throws IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException;
}
