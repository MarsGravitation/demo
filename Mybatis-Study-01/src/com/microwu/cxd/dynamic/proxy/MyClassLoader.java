package com.microwu.cxd.dynamic.proxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Description: 自定义类加载器
 * Author:   Administration
 * Date:     2019/2/27 17:29
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class MyClassLoader extends ClassLoader{
    File dir;

    // 文件路径通过字符串传递进来
    public MyClassLoader(String path){
        this.dir = new File(path);
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 路径可用
        if(dir != null){
            File classFile = new File(dir, name + ".class");
            if(classFile.exists()){
                // 字节码文件存在
                try {
                    // 把字节码文件加载到虚拟机中
                    FileInputStream in = new FileInputStream(classFile);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = in.read(buffer)) != -1){
                        outputStream.write(buffer, 0, len);
                    }
                    // 将buffer中的字节读取到内存中加载为class
                    return defineClass("proxy." + name, outputStream.toByteArray(), 0, outputStream.size());
                }catch(IOException e){
                    e.printStackTrace();
                }

            }
        }
        return super.findClass(name);
    }
}