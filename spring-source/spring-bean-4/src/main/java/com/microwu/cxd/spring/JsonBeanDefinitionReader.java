package com.microwu.cxd.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: 类似 XmlBeanDefinitionReader，只是本类是从json 文件里读取 bean definition
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   17:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JsonBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private final ThreadLocal<Set<EncodedResource>> resourceCurrentlyBeingLoaded = new NamedThreadLocal<Set<EncodedResource>>("json bean definition resources currently being loaded");

    /**
     * Create a new AbstractBeanDefinitionReader for the given bean factory.
     * <p>If the passed-in bean factory does not only implement the BeanDefinitionRegistry
     * interface but also the ResourceLoader interface, it will be used as default
     * ResourceLoader as well. This will usually be the case for
     * {@link ApplicationContext} implementations.
     * <p>If given a plain BeanDefinitionRegistry, the default ResourceLoader will be a
     * {@link PathMatchingResourcePatternResolver}.
     * <p>If the passed-in bean factory also implements {EnvironmentCapable} its
     * environment will be used by this reader.  Otherwise, the reader will initialize and
     * use a {StandardEnvironment}. All ApplicationContext implementations are
     * EnvironmentCapable, while normal BeanFactory implementations are not.
     *
     * @param registry the BeanFactory to load bean definitions into,
     *                 in the form of a BeanDefinitionRegistry
     * @see #setResourceLoader
     * @see #setEnvironment
     */
    public JsonBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        // 以下抄 XmlBeanDefinitionReader
        Set<EncodedResource> currentResources = this.resourceCurrentlyBeingLoaded.get();
        if (currentResources == null) {
            currentResources = new HashSet<EncodedResource>(4);
            this.resourceCurrentlyBeingLoaded.set(currentResources);
        }

        EncodedResource encodedResource = new EncodedResource(resource);
        if(!currentResources.add(encodedResource)) {
            throw new BeanDefinitionStoreException("Detected cyclic loading of " + encodedResource + "- check your import definitions!");
        }
        // 结束

        // 这里 encodedResource.getResource 就是我们的json 文件，这里通过spring core 工具类读取
        String json;
        try (InputStream inputStream = encodedResource.getResource().getInputStream()) {
            json = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.out.println(e);
            return 0;
        } finally {
            currentResources.remove(encodedResource);
            if (currentResources.isEmpty()) {
                this.resourceCurrentlyBeingLoaded.remove();
            }
        }

        // 使用 fastjson 解析 json 字符串
        List<GenericBeanDefinition> list = JSON.parseArray(json, GenericBeanDefinition.class);
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        /**
         * 1. GenericBeanDefinition 只有setBeanClassName，所以bean 序列化时，只序列化了这个字段
         *      实际上我们知道，beanClass 很重要，这里我们自己处理一下
         * 2.
         */
        for (GenericBeanDefinition genericBeanDefinition : list) {
            // 1. 处理 beanClass
            Class<?> clazz = null;
            try {
                clazz = Thread.currentThread().getContextClassLoader().loadClass(genericBeanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                System.out.println(e);
                throw new RuntimeException();
            }

            genericBeanDefinition.setBeanClass(clazz);

            /**
             * 2. 处理 constructor 问题
             *      因为 valueHolder.getValue()
             *      是 Object类型，但这个实际上是可变类型，当构造器参数为String，这个 Object 就是String
             *      如果构造器参数类型为其他bean 的引用时，这个object 就是 RuntimeBeanReference
             *
             *      这里把 JSONObject 转换为 RuntimeBeanReference
             */
            ConstructorArgumentValues constructorArgumentValues = genericBeanDefinition.getConstructorArgumentValues();
            if (CollectionUtils.isEmpty(currentResources)) {
                continue;
            }
            Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();
            // 我这里测试的时候，序列化 indexedArgumentValues 失败，所以 Controller 构造失败
            if (CollectionUtils.isEmpty(indexedArgumentValues)) {
                continue;
            }
            for (ConstructorArgumentValues.ValueHolder valueHolder : indexedArgumentValues.values()) {
                Object value = valueHolder.getValue();
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    RuntimeBeanReference runtimeBeanReference = jsonObject.toJavaObject(RuntimeBeanReference.class);
                    valueHolder.setValue(runtimeBeanReference);
                }
            }

        }

        // 使用 BeanNameGenerator
        setBeanNameGenerator(new AnnotationBeanNameGenerator());
        BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
        // 获取 BeanDefinitionRegistry， bean factory 默认实现了 BeanDefinitionRegistry
        BeanDefinitionRegistry registry = getRegistry();
        // 注册 bean definition 到BeanDefinitionRegistry 里面去
        for (GenericBeanDefinition genericBeanDefinition : list) {
            String beanName = beanNameGenerator.generateBeanName(genericBeanDefinition, registry);
            registry.registerBeanDefinition(beanName, genericBeanDefinition);
        }
        return list.size();
    }
}