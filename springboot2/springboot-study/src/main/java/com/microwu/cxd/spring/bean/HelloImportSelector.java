package com.microwu.cxd.spring.bean;

import com.microwu.cxd.spring.domain.Hello;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Description: ImportSelector
 * @Configuration 可以使用 @Import 引入其他配置类，还可以引入 ImportSelector
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   11:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HelloImportSelector implements ImportSelector {

    /**
     * 用于指定需要注册为 bean 的 Class 名称
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  11:21
     *
     * @param   	importingClassMetadata
     * @return  java.lang.String[]
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//        return new String[]{Hello.class.getName()};

        // 可以获取到 @Import 标注的 Class(com.microwu.cxd.spring.bean.SpringConfiguration) 的信息
//        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ComponentScan.class.getName());
//        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        // 也可以直接扫描 Class 的包 - Class.forName(importingClassMetadata.getClassName()).getPackage().getName()
        // 更通用的是两者都可以
        String[] basePackages = null;
        if (importingClassMetadata.hasAnnotation(ComponentScan.class.getName())) {
            Map<String, Object> annotationAttributes =
                    importingClassMetadata.getAnnotationAttributes(ComponentScan.class.getName());
            basePackages = (String[]) annotationAttributes.get("basePackages");
        }
        if (basePackages == null || basePackages.length == 0) {
            try {
                String basePackage = Class.forName(importingClassMetadata.getClass().getName()).getPackage().getName();
                basePackages = new String[]{basePackage};
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        AssignableTypeFilter filter = new AssignableTypeFilter(Hello.class);
        scanner.addIncludeFilter(filter);
        Set<String> classes = new HashSet<>();
        for (String basePackage : basePackages) {
            scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> classes.add(beanDefinition.getBeanClassName()));
        }
        return classes.toArray(new String[classes.size()]);
    }
}