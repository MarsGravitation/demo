package com.microwu.cxd.servlet;

import com.microwu.cxd.annotation.MyController;
import com.microwu.cxd.annotation.MyRequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   16:21
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    private Map<String, Object> controllerMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1. 加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 2. 初始化所有相关联的类, 扫描用户设定的包下面的所有类
        doScanner(properties.getProperty("scanPackage"));

        // 3. 拿到扫描的类, 通过反射机制, 实例化, 并且放到ioc容器 beanName默认是首字母小写
        doInstance();

        // 4. 初始化HandlerMapper(将url和method对应上)
        initHandlerMapping();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(handlerMapping.isEmpty()) {
            return;
        }

        String url = request.getRequestURI();
        String contextPath = request.getContextPath();

        url = url.replace(contextPath, "").replaceAll("/+", "/");

        if(!this.handlerMapping.containsKey(url)) {
            response.getWriter().write("404 NOT FOUND!");
        }

        Method method = this.handlerMapping.get(url);

        // 获取方法的参数列表
        Class<?>[] parameterTypes = method.getParameterTypes();

        // 获取请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 保存参数值
        Object[] paramValues = new Object[parameterTypes.length];

        // 方法的参数列表
        for(int i = 0; i < parameterTypes.length; i++) {
            // 根据参数名称, 做某些处理
            String requestParam = parameterTypes[i].getSimpleName();

            if(requestParam.equals("HttpServletRequest")) {
                // 参数类型已明确, 这边强转类型
                paramValues[i] = request;
                continue;
            }

            if(requestParam.equals("HttpServletResponse")) {
                paramValues[i] = response;
                continue;
            }

            if(requestParam.equals("String")) {
                for(Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "".replaceAll(".\\s", ","));
                    paramValues[i] = value;
                }
            }

        }

        // 利用反射机制来调用
        try {
            method.invoke(this.controllerMap.get(url), paramValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void doLoadConfig(String location) {
        // 把web.xml的contextConfigLocation对应的value值文件加载到流里面
        InputStream resourceAsStream = this.getClass().getResourceAsStream(location);

        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doScanner(String packageName) {
        // 把所有的 . 替换成 /
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for(File file : dir.listFiles()) {
            if(file.isDirectory()) {
                // 递归读取包
                doScanner(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doInstance() {
        if(classNames.isEmpty()) {
            return;
        }
        for(String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(MyController.class)) {
                    ioc.put(toLowerFirstWord(clazz.getSimpleName()), clazz.newInstance());
                } else {
                    continue;
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void initHandlerMapping() {
        if(ioc.isEmpty()) {
            return;
        }
        for(Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<? extends Object> clazz = entry.getValue().getClass();
            if(clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }

            // 拼url时, 是Controller头的URL上拼上方法的url
            String baseUrl = "";
            if(clazz.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
                baseUrl = annotation.value();
            }
            Method[] methods = clazz.getMethods();
            for(Method method : methods) {
                if(!method.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }
                MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
                String url = annotation.value();

                url = (baseUrl + "/" + url).replaceAll("/+", "/");
                handlerMapping.put(url, method);
                try {
                    controllerMap.put(url, clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

}