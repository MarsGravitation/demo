package com.microwu.cxd.mybatis.v2.session;

import com.microwu.cxd.mybatis.v2.TestMybatis;
import com.microwu.cxd.mybatis.v2.annotation.Pojo;
import com.microwu.cxd.mybatis.v2.annotation.Select;
import com.microwu.cxd.mybatis.v2.binding.MapperRegister;
import com.microwu.cxd.mybatis.v2.executor.CacheExecutor;
import com.microwu.cxd.mybatis.v2.executor.CustomExecutor;
import com.microwu.cxd.mybatis.v2.executor.SimpleExecutor;
import com.microwu.cxd.mybatis.v2.plugin.CustomInterceptorChain;
import com.microwu.cxd.mybatis.v2.plugin.Interceptor;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:   Administration
 * Date:     2019/2/28 10:33
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CustomConfiguration {
    public static final MapperRegister mapperRegister = new MapperRegister();
    public static final Map<String, String> mappedStatement = new HashMap<>();

    private CustomInterceptorChain customInterceptorChain = new CustomInterceptorChain();
    private boolean enableCache = false;
    private List<Class<?>> mapperList = new ArrayList<>();
    private List<String> classPaths = new ArrayList<>();

    /**
     * @Descrip 初始化configuration时，加载所有的Mapper，plugin以及是否需要缓存
     * @author 成旭东
     * @date 2019/2/28 13:59
     * @param  * @param mapperPath
     * @param pluginPath
     * @param enableCache
     * @return
     */
    public CustomConfiguration(String mapperPath, String[] pluginPath, boolean enableCache) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 扫描mapper路径，将必要的mapper信息存入mapperRegister和mapperStatement
        scanPackage(mapperPath);
        for(Class<?> mapper : mapperList){
            // 当类为接口时，开始解析
            if(mapper.isInterface()){
                parsingClass(mapper);
            }
        }
        if(pluginPath != null){
            // 遍历plugin路径，初始化plugin并放入list中
            for(String plugin : pluginPath){
                Interceptor interceptor = (Interceptor) Class.forName(plugin).newInstance();
                customInterceptorChain.addInterceptor(interceptor);
            }
        }
        // 设置缓存是否开启
        this.enableCache = enableCache;

    }

    /**
     * @Descrip MapperProxy根据StatementName查找是否有对应的SQL
     * @author 成旭东
     * @date 2019/2/28 14:45
     * @param  * @param statementName
     * @return boolean
     */
    public boolean hasStatement(String statementName){
        return mappedStatement.containsKey(statementName);
    }

    /**
     * @Descrip 根据statementId获取SQL
     * @author 成旭东
     * @date 2019/2/28 14:46
     * @param  * @param id
     * @return java.lang.String
     */
    public String getMappedStatement(String id){
        return mappedStatement.get(id);
    }

    public <T> T getMapper(Class<T> clazz, CustomSqlSession sqlSession){
        return mapperRegister.getMapper(clazz, sqlSession);
    }

    /**
     * 创建一个Executor(因为加入了plugin功能，需要判断是否创建带plugin的executor)
     */
    public CustomExecutor newExecutor() {
        CustomExecutor executor = createExecutor();
        if (customInterceptorChain.hasPlugin()) {
            return (CustomExecutor)customInterceptorChain.pluginAll(executor);
        }
        return executor;
    }

    /**
     * @Descrip 创建一个Executor（判断是否需要加缓存）
     * @author 成旭东
     * @date 2019/2/28 14:42
     * @param  * @param
     * @return com.microwu.cxd.mybatis.v1.CustomExecutor
     */
    private CustomExecutor createExecutor(){
        if(enableCache){
            return new CacheExecutor(new SimpleExecutor());
        }
        return new SimpleExecutor();
    }
    
    /**
     * @Descrip 解析类中的注解
     * @author 成旭东
     * @date 2019/2/28 14:19
     * @param  * @param mapper 
     * @return void
     */
    private void parsingClass(Class<?> mapper) {
        // 如有pojo注解，解析获取实体类信息
        if(mapper.isAnnotationPresent(Pojo.class)){
            for(Annotation annotation : mapper.getAnnotations()){
                if(annotation.annotationType().equals(Pojo.class)){
                    // 将mapper的信息注册到register中
                    mapperRegister.addMapper(mapper, ((Pojo)annotation).value());
                }
            }
        }

        Method[] methods = mapper.getMethods();
        for (Method method : methods){
            // 如果有select 注解就解析
            if(method.isAnnotationPresent(Select.class)){
                for(Annotation annotation : method.getDeclaredAnnotations()){
                    if(annotation.annotationType().equals(Select.class)){
                        // 将方法名和SQL语句注册到mappedStatement中
                        mappedStatement.put(method.getDeclaringClass() + "." + method.getName(), ((Select)annotation).value());
                    }
                }
            }
        }
    }

    /**
     * @Descrip 扫描包名，获取包下面的所有.class文件
     * @author 成旭东
     * @date 2019/2/28 14:01
     * @param  * @param mapperPath
     * @return void
     */
    private void scanPackage(String mapperPath) throws ClassNotFoundException {
        String classPath = TestMybatis.class.getResource("/").getPath();
        mapperPath = mapperPath.replace(".", File.separator);
        String mainPath = classPath + mapperPath;
        doPath(new File(mainPath));
        for(String className : classPaths){
            // 获取类的完全限定名
            className = className.replace(classPath.replace("/","\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");
            Class<?> clazz = Class.forName(className);
            mapperList.add(clazz);
        }
    }

    /**
     * @Descrip 该方法会得到所有的类，将类的绝对路径写入到classPaths中
     * @author 成旭东
     * @date 2019/2/28 14:05
     * @param  * @param file
     * @return void
     */
    private void doPath(File file){
        if(file.isDirectory()){
            // 文件夹
            File[] files = file.listFiles();
            for(File f : files){
                doPath(f);;
            }
        }else{
            // 文件
            if(file.getName().endsWith(".class")){
                // class文件
                classPaths.add(file.getPath());
            }
        }
    }
}