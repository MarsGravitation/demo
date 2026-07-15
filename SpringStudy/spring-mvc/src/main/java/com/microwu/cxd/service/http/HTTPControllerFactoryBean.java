package com.microwu.cxd.service.http;

import com.microwu.cxd.annotation.HTTPController;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/1/31   11:10
 * Update History:
 * Author        Time            Content
 */
public class HTTPControllerFactoryBean implements FactoryBean<Object> {

    // 目标接口
    private Class<?> targetClass;

    private HTTPController.ThirdPartEnum thirdPart;

    private String baseUrl;

    private Class<InvocationHandler> invocationHandlerClass;

    @Override
    public Object getObject() throws Exception {
        InvocationHandler invocationHandler;
        try {
            invocationHandler = invocationHandlerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("[HTTPControllerFactoryBean-invocationHandlerClass-newInstance] error", e);
        }
        // 通过Proxy将代理类对象转成目标接口
        return Proxy.newProxyInstance(HTTPControllerFactoryBean.class.getClassLoader()
                , new Class[]{targetClass}
                , invocationHandler);
    }

    /**
     * 返回此 FactoryBean 生成的对象类型
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/1/31 11:15
     *
     * @param:
     * @return: java.lang.Class<?>
     */
    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }

    /**
     * 表示此 FactoryBean 生成的对象是否为单例
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/1/31 11:14
     *
     * @param:
     * @return: boolean
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
