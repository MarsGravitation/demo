package com.microwu.cxd.mybatis.v1;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 自定义CustomConfiguration实现getMapper方法，
 *              初始化MapperProxyFactory存放所有的Mapper，
 *              根据class获取MapperProxyFactory
 * Author:   Administration
 * Date:     2019/2/26 16:54
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CustomConfiguration {
    public final MapperRegistory mapperRegistory = new MapperRegistory();
    public static final Map<String, String> mappedStatement = new HashMap<>();

    /**
     * @Descrip 初始化时加载所有的Mapper方法和SQL语句
     * TODO：改用annotation扫描，现在使用HardCode
     * @author 成旭东
     * @date 2019/2/26 16:59
     * @param  * @param
     * @return
     */
    public CustomConfiguration() {
        mapperRegistory.addMapper(TestCustomMapper.class);
        mappedStatement.put("com.microwu.cxd.mybatis.v1.TestCustomMapper.selectByPrimaryByKey",
                "select * from user where id = %d");
    }

    /**
     * @Descrip MapperProxy根判断statementName是否存在对应的SQL
     * @author 成旭东
     * @date 2019/2/26 17:06
     * @param  * @param statementKey
     * @return boolean
     */
    public boolean hasStatement(String statementName){
        return mappedStatement.containsKey(statementName);
    }

    /**
     * @Descrip MapperProxy根据id获取对应的SQL语句
     * @author 成旭东
     * @date 2019/2/26 17:08
     * @param  * @param id
     * @return java.lang.String
     */
    public String getStatement(String id){
        return mappedStatement.get(id);
    }

    public <T> T getMapper(Class<T> clazz, CustomSqlSession customSqlSession) {
        return mapperRegistory.getMapper(clazz, customSqlSession);
    }
}