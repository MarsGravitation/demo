package com.microwu.cxd.reflex;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Description:     反射Demo
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/20   10:18
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReflexDemo {
    private static String[] methods = new String[]{"mobile", "userType"};
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String mobile = "18435202728";
        System.out.println(stringToBean(mobile, User.class));
    }

    public static <T> T stringToBean(String mobile, Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(mobile.isEmpty()) {
            return null;
        }
        T t = clazz.newInstance();
        for(String m : methods) {
            // 生成方法名
            String methodName = "set" + m.substring(0, 1).toUpperCase() + m.substring(1);
            Field field = getClassField(clazz, m);
            if(field == null) continue;
            Class<?> fieldType = field.getType();
            Object value = null;
            if(m.equals("mobile")) {
                 value = convertValType(mobile, fieldType);
            }else if(m.equals("userType")) {
                value = convertValType(4, fieldType);
            }
            clazz.getMethod(methodName, field.getType()).invoke(t, value);

        }
        return t;
    }

    /**
     *  根据字段名称查找对应的Field对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/5/20  10:36
     *
     * @param   	clazz
     * @param 		fieldName
     * @return  java.lang.reflect.Field
     */
    public static <T> Field getClassField(Class<T> clazz, String fieldName) {
        if(Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field : declaredFields) {
            if(field.getName().equals(fieldName)) {
                return field;
            }
        }
        // 递归父类
        Class<? super T> superclass = clazz.getSuperclass();
        if(superclass != null) {
            return getClassField(superclass, fieldName);
        }
        return null;
    }

    /**
     *  将Object类型的值，转换成Bean对象属性对应的类型值
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/5/20  10:44
     *
     * @param   	value
     * @param 		fieldTypeClass
     * @return  java.lang.Object
     */
    public static Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;
        if(Long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if(Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if(Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }
}