package com.microwu.cxd.mybatis.v2.binding;

import com.microwu.cxd.mybatis.v2.session.CustomSqlSession;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: Mapper的注册类
 * Author:   Administration
 * Date:     2019/3/1 11:33
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class MapperRegister {
    // 用一个map维护所有的Mapper
    private final Map<Class<?>, MapperProxyFactory> knowMappers = new HashMap<>();

    /**
     * @Descrip configuration解析annotation后，调用此方法
     * @author 成旭东
     * @date 2019/3/1 11:53
     * @param  * @param clazz
     * @param pojo
     * @return void
     */
    public <T> void addMapper(Class<T> clazz, Class pojo){
        knowMappers.put(clazz, new MapperProxyFactory(clazz, pojo));
    }

    /**
     * @Descrip getMapper的最底层执行者，获取ProxyFactory来生成代理对象
     * @author 成旭东
     * @date 2019/3/1 11:57
     * @param  * @param clazz
     * @param sqlSession
     * @return T
     */
    public <T> T getMapper(Class<T> clazz, CustomSqlSession sqlSession){
        MapperProxyFactory proxyFactory = knowMappers.get(clazz);
        if(proxyFactory == null){
            throw new RuntimeException("Type:" + clazz + "can not find");
        }
        return (T)proxyFactory.newInstance(sqlSession);
    }

    private class MapperProxyFactory<T>{
        private Class<T> mapperInterface;
        private Class object;

        public MapperProxyFactory(Class<T> mapperInterface, Class object) {
            this.mapperInterface = mapperInterface;
            this.object = object;
        }

        public T newInstance(CustomSqlSession sqlSession){
            return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, new MapperProxy(sqlSession, object));
        }
    }

}