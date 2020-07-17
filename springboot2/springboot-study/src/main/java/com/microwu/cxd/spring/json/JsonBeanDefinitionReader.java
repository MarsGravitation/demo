package com.microwu.cxd.spring.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
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
 * Description: 类似 XmlBeanDefinitionReader，只是本类是从json文件中读取bean definition
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   10:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JsonBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private final ThreadLocal<Set<EncodedResource>> resourceCurrentlyBeingLoaded = new NamedThreadLocal<>("json bean definition resource currently being loaded");

    protected JsonBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        // 照抄XmlBeanDefinitionReader 开始
        Set<EncodedResource> currentResources = this.resourceCurrentlyBeingLoaded.get();
        if (currentResources == null) {
            currentResources = new HashSet<>(4);
            this.resourceCurrentlyBeingLoaded.set(currentResources);
        }

        EncodedResource encodedResource = new EncodedResource(resource);
        if (!currentResources.add(encodedResource)) {
            throw new BeanDefinitionStoreException( "Detected cyclic loading of " + encodedResource + " - check your import definitions!");
        }
        // 结束

        // 这里读取json 文件，通过工具类转换成json 字符串
        String json;
        InputStream inputStream;
        try {
            inputStream = encodedResource.getResource().getInputStream();
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

        // 这个案例没跑通，这里面json 解析数据不全，具体原因没看
        List<GenericBeanDefinition> list = JSON.parseArray(json, GenericBeanDefinition.class);
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        // 1. GenericBeanDefinition 只有setBeanClassName，所以bean 反序列化，只序列化了这个字段
        for (GenericBeanDefinition genericBeanDefinition : list) {
            // 1. 处理 beanClass
            Class<?> clazz;
            try {
                clazz = Thread.currentThread().getContextClassLoader().loadClass((genericBeanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                System.out.println(e);
                throw new RuntimeException();
            }

            genericBeanDefinition.setBeanClass(clazz);

            // 2. 处理constructor 问题
            // 因为valueHolder.getValue 是Object，当其实应该是可变类型的
            // 当构造器参数为String，这个Object 就是 String 类型，当构造器参数是引用时，就是RuntimeBeanReference
            ConstructorArgumentValues constructorArgumentValues = genericBeanDefinition.getConstructorArgumentValues();
            if (constructorArgumentValues.isEmpty()) {
                continue;
            }
            Map<Integer, ConstructorArgumentValues.ValueHolder> map = constructorArgumentValues.getIndexedArgumentValues();
            for (ConstructorArgumentValues.ValueHolder valueHolder : map.values()) {
                Object value = valueHolder.getValue();
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    RuntimeBeanReference runtimeBeanReference = jsonObject.toJavaObject(RuntimeBeanReference.class);
                    valueHolder.setValue(runtimeBeanReference);
                }
            }

        }

        // 这里new 一个BeanNameGenerator，这个是自带的
        setBeanNameGenerator(new AnnotationBeanNameGenerator());
        BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
        // 获取BeanDefinitionRegistry， bean factory 默认实现了BeanDefinitionRegistry
        BeanDefinitionRegistry registry = getRegistry();
        // 注册bean definition 到BeanDefinitionRegistry 中
        for (GenericBeanDefinition genericBeanDefinition : list) {
            String beanName = beanNameGenerator.generateBeanName(genericBeanDefinition, registry);
            registry.registerBeanDefinition(beanName, genericBeanDefinition);
        }

        return list.size();
    }
}