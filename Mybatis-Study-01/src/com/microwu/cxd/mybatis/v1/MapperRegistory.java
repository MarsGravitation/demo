package com.microwu.cxd.mybatis.v1;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: Mapper的注册类
 * Author:   Administration
 * Date:     2019/2/26 17:10
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class MapperRegistory{
    // 用mapper维护所有的Mapper
    private final Map<Class<?>, MapperProxyFactory> knowMapper = new HashMap<>();

    /**
     * @Descrip configuration解析annotation调用方法初始化所有的Mapper
     * @author 成旭东
     * @date 2019/2/26 17:28
     * @param  * @param clazz
     * @return void
     */
    public <T> void addMapper(Class<T> clazz){
        knowMapper.put(clazz, new MapperProxyFactory(clazz));
    }

    public <T> T getMapper(Class<T> clazz, CustomSqlSession sqlSession){
        MapperProxyFactory factory = knowMapper.get(clazz);
        if(factory == null){
            throw new RuntimeException("Type:" + clazz + " not find");
        }
        return (T)factory.newInstance(sqlSession);
    }

    /**
     * @Descrip 内部类实现一个Factory生成代理对象
     * @author 成旭东
     * @date 2019/2/27 10:34
     */
    public class MapperProxyFactory<T>{
        private Class<T> mapperInterface;

        public MapperProxyFactory(Class<T> mapperInterface) {
            this.mapperInterface = mapperInterface;
        }

        public T newInstance(CustomSqlSession sqlSession){
            return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new MapperProxy(sqlSession));
        }
    }
}