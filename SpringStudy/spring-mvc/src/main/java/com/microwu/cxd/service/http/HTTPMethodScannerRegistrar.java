package com.microwu.cxd.service.http;

import com.microwu.cxd.annotation.HTTPController;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

/**
 * Description:
 *
 * 在 ImportBeanDefinitionRegistrar 接口中，有一个 registerBeanDefinitions 方法，通过该
 * 方法可以向 Spring 容器中注册 Bean 实例
 *
 * 实现该接口的类都会被 ConfigurationClassPostProcessor 后置处理器，因此在
 * ImportBeanDefinitionRegistrar 注册的 Bean 可以比依赖它的 Bean 更在初始化
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   11:16
 * Update History:
 * Author        Time            Content
 */
public class HTTPMethodScannerRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 注入对象到 Spring
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/1/31 11:18
     *
     * @param: annotationMetadata - [org.springframework.core.type.AnnotationMetadata]
     * @param: beanDefinitionRegistry - [org.springframework.beans.factory.support.BeanDefinitionRegistry]
     * @return: void
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        // ClassPathScanningCandidateComponentProvider 是 Spring 提供的工具，可以按自定义的类型，查找 classpath 下符合要求的 class 文件
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
                return super.isCandidateComponent(metadataReader);
            }
        };
        // 指定扫描的包名，在该包路径下带有 @HTTPController 注解的接口
        Set<BeanDefinition> beanDefinitionSet =
                componentProvider.findCandidateComponents("");

        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            // 注入处理
            registerBeanDefinition((AnnotatedBeanDefinition) beanDefinition, beanDefinitionRegistry);
        }
    }

    /**
     * 将扫描到的接口放置 DefaultListableBeanFactory 的 beanDefinitionMap 中
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/1/31 11:22
     *
     * @param: beanDefinition - [org.springframework.beans.factory.annotation.AnnotatedBeanDefinition]
     * @param: beanDefinitionRegistry - [org.springframework.beans.factory.support.BeanDefinitionRegistry]
     * @return: void
     */
    private void registerBeanDefinition(AnnotatedBeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) {
        // 接口元数据
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        // 接口全类名
        String className = metadata.getClassName();

        // 生成一个 HTTPControllerFactoryBean 的 BeanDefinition
        AbstractBeanDefinition factoryBeanBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(HTTPControllerFactoryBean.class).getBeanDefinition();
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(HTTPController.class.getName()));
        // requiredType： java.lang.Class，convertedValue： "com.zhuanzhuan.zzpayaccount.gateway.WeiXinPayRequestGateway"
        // FactoryBean的targetClass是Class<?>类型，但这里可以用“类全路径”字符串表示，
        // 是因为Spring在初始化bean的时候可以根据setTargetClass方法的参数来判断类型，进而将“类全路径”字符串转为Class<?>类型
        factoryBeanBeanDefinition.getPropertyValues().add("targetClass", className);
        factoryBeanBeanDefinition.getPropertyValues().add("baseUrl", annotationAttributes.getString("baseUrl"));
        factoryBeanBeanDefinition.getPropertyValues().add("thirdPart", annotationAttributes.get("thirdPart"));
        factoryBeanBeanDefinition.getPropertyValues().add("invocationHandlerClass", annotationAttributes.get("invocationHandlerClass"));

        // className作为 beanName，可以自定义前后缀，如className + "$ByScanner"
        beanDefinitionRegistry.registerBeanDefinition(className, factoryBeanBeanDefinition);
    }
}
