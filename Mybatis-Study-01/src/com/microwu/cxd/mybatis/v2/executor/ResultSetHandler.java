package com.microwu.cxd.mybatis.v2.executor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Description: 负责结果映射
 * Author:   Administration
 * Date:     2019/2/28 15:50
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ResultSetHandler {

    /**
     * @Descrip 细化了职责，专门负责结果映射
     * @author 成旭东
     * @date 2019/2/28 15:52
     * @param  * @param rs
     * @param pojo
     * @return T
     */
    public <T> T handle(ResultSet rs, Class pojo) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, NoSuchMethodException {
        // 代替了ObjectFactory
        Object o = pojo.newInstance();
        // 遍历结果集，设置对象属性值
        while(rs.next()){
            for(Field field : o.getClass().getFields()){
                setValue(o, field, rs);
            }
        }
        return (T)o;
    }

    /**
     * @Descrip 利用反射给每一个属性设值
     * @author 成旭东
     * @date 2019/2/28 15:56
     * @param  * @param o
     * @param field
     * @param rs
     * @return void
     */
    private void setValue(Object o, Field field, ResultSet rs) throws InvocationTargetException, IllegalAccessException, SQLException, NoSuchMethodException {
        Method method = o.getClass().getMethod("set" + firstWordCapital(field.getName()), field.getType());
        method.invoke(o, getResult(rs, field));
    }

    /**
     * @Descrip 将单词的首字母大写
     * @author 成旭东
     * @date 2019/2/28 16:04
     * @param  * @param name
     * @return java.lang.String
     */
    private String firstWordCapital(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * @Descrip 根据反射类型，从rs中获取参数
     * @author 成旭东
     * @date 2019/2/28 16:01
     * @param  * @param rs
     * @param field
     * @return java.lang.Object
     */
    private Object getResult(ResultSet rs, Field field) throws SQLException {
        if(field.getType() == Integer.class){
            return rs.getInt(field.getName());
        }else if(field.getType() == String.class){
            return rs.getString(field.getName());
        }
        return null;
    }
}