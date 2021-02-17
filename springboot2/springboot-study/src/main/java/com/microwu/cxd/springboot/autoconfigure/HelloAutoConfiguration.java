package com.microwu.cxd.springboot.autoconfigure;

import com.microwu.cxd.spring.domain.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: 自定义自动配置
 *  ConditionalOnClass ：用于指定在Classpath下拥有某些Class时才生效
 *  ConditionalOnMissingClass ：用于指定在Classpath下不存在某些Class时才生效
 *  ConditionalOnBean ：用于指定在bean容器中存在某些bean时生效
 *  ConditionalOnMissingBean ：用于指定在bean容器中不存在某些bean时生效
 *  ConditionalOnWebApplication ：用于指定当应用是Web应用时生效
 *  ConditionalOnNotWebApplication ：用于指定当应用是非Web应用时生效
 *  ConditionalOnProperty ：用于指定当配置了某些特定的参数时生效
 *  ConditionalOnExpression ：用于根据SpEl表达式控制是否生效
 *  ConditionalOnSingleCandidate ：用于指定当bean容器中只存在唯一的指定类型的bean时才生效；当bean容器中存在多个指定类型的bean，但是使用@Primary指定了主候选者也是可以匹配的，即也是生效的
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/22   14:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@ConditionalOnClass(Hello.class)
@ConditionalOnMissingBean(HelloBean.class)
@EnableConfigurationProperties(HelloProperties.class)
public class HelloAutoConfiguration {

    @Autowired
    private HelloProperties helloProperties;

    /**
     * 当 Environment 存在 key 为 autoconfigure.hello.enabled 属性且其值为 true 才会创建
     * matchIfMissing 如果不存在也会创建
     *
     * @Retention(RetentionPolicy.RUNTIME)
     * @Target({ ElementType.TYPE, ElementType.METHOD })
     * @Documented
     * @Conditional(OnPropertyCondition.class)
     * public @interface ConditionalOnProperty {
     *
     *     String[] value() default {}; //数组，获取对应property名称的值，与name不可同时使用
     *
     *     String prefix() default "";//property名称的前缀，可有可无
     *
     *     String[] name() default {};//数组，property完整名称或部分名称（可与prefix组合使用，组成完整的property名称），与value不可同时使用
     *
     *     String havingValue() default "";//可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置
     *
     *     boolean matchIfMissing() default false;//缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错
     *
     *     boolean relaxedNames() default true;//是否可以松散匹配，至今不知道怎么使用的
     * }
     * }
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/22  14:11
     *
     * @param
     * @return  com.microwu.cxd.springboot.autoconfigure.HelloBean
     */
    @Bean
    @ConditionalOnProperty(prefix = "autoconfigure.hello", name = "enabled", havingValue = "true", matchIfMissing = true)
    public HelloBean helloBean() {
        HelloBean helloBean = new HelloBean();
        helloBean.setName(helloProperties.getName());
        return helloBean;
    }

}