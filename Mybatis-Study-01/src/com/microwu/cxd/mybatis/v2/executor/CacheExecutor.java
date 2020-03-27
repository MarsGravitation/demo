package com.microwu.cxd.mybatis.v2.executor;

import com.microwu.cxd.mybatis.v2.cache.CacheKey;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 缓存的Executor，使用了装饰者模式
 * Author:   Administration
 * Date:     2019/3/1 9:08
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CacheExecutor implements CustomExecutor {
    private CustomExecutor delegate;
    private static final Map<Integer, Object> cache = new HashMap<>();

    /**
     * @Descrip 装饰Executor，在原有的Executor添加缓存功能
     * @author 成旭东
     * @date 2019/3/1 9:13
     */
    public CacheExecutor(CustomExecutor delegate) {
        this.delegate = delegate;
    }

    /**
     * @param *         @param statement
     * @param parameter
     * @param pojo
     * @return T
     * @Descrip 修改了查询方法，添加了缓存功能
     * 由于没有涉及增删改，所以没有清除缓存
     * @author 成旭东
     * @date 2019/3/1 9:15
     */
    @Override
    public <T> T query(String statement, String parameter, Class pojo)
            throws IllegalAccessException, InstantiationException, SQLException,
            NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
        // 这里的CacheKey为缓存的关键
        CacheKey cacheKey = new CacheKey();
        // 根据SQL语句和参数两个维度来判断此次查询是否相同
        // 底层原理为hashcode的计算
        // Mybatis中cacheKey的code有更多的维度，这里体现思想即可
        cacheKey.update(statement);
        cacheKey.update(parameter);

        // 判断map中是否有根据SQL语句和参数算出来的code
        if(!cache.containsKey(cacheKey.getCode())){
            // 第一次查询，使用被装饰者查询数据，并把数据存入缓存
            Object object = delegate.query(statement, parameter, pojo);
            cache.put(cacheKey.getCode(), object);
            return (T)object;
        }
        System.out.println("从缓存中获取数据~~~");
        return (T)cache.get(cacheKey.getCode());
    }
}