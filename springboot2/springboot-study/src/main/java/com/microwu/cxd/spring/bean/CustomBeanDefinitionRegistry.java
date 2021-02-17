package com.microwu.cxd.spring.bean;

import com.microwu.cxd.spring.domain.Hello;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * Description: BeanDefinitionRegistryPostProcessor 是一种比较特殊的 BeanFactoryPostProcessor，可以让我们自定义的注册 bean 定义的逻辑
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   9:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CustomBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

    /**
     * 可以使用 ClassPathScanningCandidateComposeProvider 使用
     * 作用：可以根据一定规则扫描类路径下满足特定条件的 Class 来作为候选的 bean 定义
     *
     * ClassPathBeanDefinitionScanner 扩展了 ClassPathScanningCandidateComposeProvider
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  10:50
     *
     * @param   	registry
     * @return  void
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        RootBeanDefinition helloBean = new RootBeanDefinition(Hello.class);
//        // 新增 bean 定义
//        registry.registerBeanDefinition("hello", helloBean);

        // 默认 filter 只扫描 Component，Service 等注解
        boolean userDefaultFilters = false;
        String basePackage = "com.microwu.cxd";
        // 只会扫描，需要自己进行注册
        ClassPathScanningCandidateComponentProvider beanScanner = new ClassPathScanningCandidateComponentProvider(userDefaultFilters);
        TypeFilter includeFilter = new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                // 匹配非接口和非抽象类
                return metadataReader.getClassMetadata().isConcrete();
            }
        };

        // 扫描注解
        new AnnotationTypeFilter(Component.class);

        // 扫描 class，如果是接口的话，实现类可以被扫描到
        new AssignableTypeFilter(Hello.class);

        beanScanner.addIncludeFilter(includeFilter);
        Set<BeanDefinition> beanDefinitionSet = beanScanner.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            // beanName 通常有 BeanNameGenerator 来生成
            String name = beanDefinition.getBeanClassName();
            registry.registerBeanDefinition(name, beanDefinition);
        }

        // 可以把扫描到的自动注册
//        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
//        AnnotationTypeFilter filter = new AnnotationTypeFilter(Component.class);
//        scanner.addIncludeFilter(filter);
//        scanner.scan(basePackage);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}