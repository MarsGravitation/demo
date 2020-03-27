package com.microwu.cxd.dynamic.proxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Description: 使用字符串拼接手动生成代理类Java文件
 * Author:   Administration
 * Date:     2019/2/27 16:30
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ProxyClassBySelf {
    static String rt = "\r\n";

    private static String get$Proxy0(Class<?> interfaces){
        Method[] methods = interfaces.getMethods();
        String proxyClass = "package com.microwu.cxd.dynamic.proxy.byself;" + rt +
                "import java.lang.reflect.Method;" + rt +
                "public class $Proxy0 implements " + interfaces.getName() + " {" + rt +
                "MyInvocationHandler ih;" + rt +
                "public $Proxy0(MyInvocationHandler h){" + rt +
                "this.ih = h;" + rt +
                "}" + rt +
                getMethodString(methods, interfaces) + rt +
                "}";
        return proxyClass;
    }

    private static String getMethodString(Method[] methods, Class<?> interfaces){
        String methodString = "";
        for(Method method : methods){
            methodString += "public String " + method.getName() + "()throws Throwable{" + rt +
                    "Method md = " + interfaces.getName() + ".class.getMethod(\"" + method.getName() + "\",new Class[]{});" + rt +
                    "return (String) this.h.invoke(this, md, null);" + rt + "}" + rt;
        }
        return methodString;
    }

    private static void outputFile(String proxyClass, String path){
        File file = new File(path);
        try{
            FileWriter fw = new FileWriter(file);
            fw.write(proxyClass);
            fw.flush();
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void compileJavaFile(String fileName){
        try {
        // 获取当前系统的编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 获取文件管理者
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = manager.getJavaFileObjects(fileName);
        // 编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, javaFileObjects);
        // 开始编译
        task.call();
        // 关闭文件管理者
            manager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Object loadClassToJvm(MyInvocationHandler handler){
        MyClassLoader classLoader = new MyClassLoader("");
        try {
            Class<?> $Proxy0 = classLoader.findClass("$Proxy0");
            Constructor<?> constructor = $Proxy0.getConstructor(MyInvocationHandler.class);
            return constructor.newInstance(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}