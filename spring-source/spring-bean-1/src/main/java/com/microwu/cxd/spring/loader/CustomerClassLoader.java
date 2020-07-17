package com.microwu.cxd.spring.loader;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/8   10:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CustomerClassLoader extends ClassLoader {

    private String path = "E:\\work-note\\Products\\spring-source\\spring-bean-2\\target\\classes\\";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> cls = findLoadedClass(name);
        if (cls != null) {
            return cls;
        }

        try {
            FileInputStream is = new FileInputStream(path + name.replace(".", "/") + ".class");
            byte[] bytes = IOUtils.toByteArray(is);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.loadClass(name);
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        CustomerClassLoader ccl = new CustomerClassLoader();
        Class<?> aClass = ccl.findClass("com.microwu.cxd.spring.BeanDefinitionDemo");

        Object o = aClass.newInstance();
        System.out.println(o);

    }
}