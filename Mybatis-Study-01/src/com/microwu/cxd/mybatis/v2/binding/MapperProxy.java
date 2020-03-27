package com.microwu.cxd.mybatis.v2.binding;

import com.microwu.cxd.mybatis.v2.session.CustomSqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description: Mapper的动态代理者
 * Author:   Administration
 * Date:     2019/3/1 11:15
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class MapperProxy implements InvocationHandler {
    private CustomSqlSession sqlSession;
    private Class object;

    public MapperProxy(CustomSqlSession sqlSession, Class object) {
        this.sqlSession = sqlSession;
        this.object = object;
    }

    /**
     * @Descrip 每一个Mapper的每一个方法都将执行invoke方法
     *          此方法判断方法名是否维护在configuration中，如果有，就取出SQL语句
     * @author 成旭东
     * @date 2019/3/1 11:20
     * @param  * @param proxy
     * @param method
     * @param args
     * @return java.lang.Object
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(sqlSession.getConfiguration().hasStatement(method.getDeclaringClass().getName() + "." + method.getName())){
            String sql = sqlSession.getConfiguration().getMappedStatement(method.getDeclaringClass().getName() + "." + method.getName());
            return sqlSession.selectOne(sql, args[0].toString(), object);
        }
        return null;
    }
}