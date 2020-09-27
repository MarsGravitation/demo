package com.microwu.cxd.netty.server.utils;

import com.microwu.cxd.netty.server.annotation.CicadaAction;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:29
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassScanner {

    private static final Logger LOGGER = LoggerBuilder.getLogger(ClassScanner.class);

    private static Map<String, Class<?>> actionMap = null;
    private static Map<String, Class<?>> interceptorMap = null;

    public static Map<String, Class<?>> getCicadaAction(String packageName) throws Exception {
        if (actionMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return actionMap;
            }

            actionMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {
                if (cls.getAnnotation(CicadaAction.class) == null) {
                    continue;
                }

                Annotation[] annotations = cls.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (!(annotation instanceof CicadaAction)) {
                        continue;
                    }
                    CicadaAction cicadaAction = (CicadaAction) annotation;
                    actionMap.put(cicadaAction.value() == null ? cls.getName() : cicadaAction.value(), cls);
                }
            }
        }
        return actionMap;
    }

    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        HashSet<Class<?>> classes = new HashSet<>();
        boolean recursive = true;

        String packageDirName = packageName.replace(".", "/");

        URL url;
        url = Thread.currentThread().getContextClassLoader().getResource(packageDirName);
        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
        findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);

        return classes;
    }

    private static void findAndAddClassesInPackageByFile(String packageName, String filePath, boolean recursive, HashSet<Class<?>> classes) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory() || (file.getName().endsWith(".class")));
            }
        });

        for(File file : dirFiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}