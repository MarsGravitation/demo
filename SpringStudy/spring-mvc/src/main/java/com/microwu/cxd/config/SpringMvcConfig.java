package com.microwu.cxd.config;

import com.microwu.cxd.domain.StringToDateConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description:     SpringMVC 自定义配置:
 *      1. @EnableWebMvc + extends WebMvcConfigurationAdapter, 在扩展类中重写父类的方法, 会屏蔽@EnableAotuConfigration中的设置
 *      2. extends WebMvcConfigurationSupport, 重写父类方法, 会屏蔽默认的设置
 *      3. extends WebMvcConfigurationAdapter, 重写父类方法, 不会屏蔽默认的设置(过时) - implements WebMvcConfigurer
 *
 *      https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#integrating-thymeleaf-with-spring
 *
 * 官网的配置是：
 * @Configuration
 * @EnableWebMvc
 * public class WebConfig implements WebMvcConfigurer {
 *
 *     // Implement configuration methods...
 * }
 *
 * 一般来说不要使用 @EnableWebMVC，会使默认配置失效
 *
 * 官网提供了高级模式
 * @Configuration
 * public class WebConfig extends DelegatingWebMvcConfiguration {
 *
 *     // ...
 * }
 *
 * 高级模式说的是 @EnableWebMvc 导入 DelegatingWebMvcConfiguration，其中：
 *  为 Spring MVC 应用程序提供默认的 Spring 配置
 *  检测并委托给 WebMvcConfigurer 实现以自定义该配置
 *
 *  删除 @EnableWebMvc 直接继承 DelegatingWebMvcConfiguration 效果类似
 *
 * SpringBoot 中的 Spring MVC
 * SpringBoot 提供了自动配置：
 *  1. 包含 ContentNegotiatingViewResolver 和 BeanNameViewResolver
 *  2. 支持提供静态资源
 *  3. 自动注册 Converter, GenericConverter 和 Formatter
 *  4. 支持 HttpMessageConverters
 *  5. 自动注册 MessageCodesResolver
 *  6. 静态 index.html支持
 *  7. 自动使用 ConfigurableWebBindingInitializer
 *
 *  如果要保留这些 SpringBoot MVC 定制并进行更多的定制，可以添加自己的 @Configuration WebMvcConfigurer 类，但不添加 @EnableWebMvc
 *
 * @Author:          chengxudong             chengxudong@microwu.com
 * Date:           2019/7/31   14:15
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
//@EnableWebMvc
public class SpringMvcConfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext;

//    /**
//     * Thymeleaf 框架配置
//     *
//     * @author   chengxudong               chengxudong@microwu.com
//     * @date    2019/7/31  15:16
//     *
//     * @param
//     * @return  org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
//     */
//    @Bean
//    public SpringResourceTemplateResolver templateResolver() {
//        // SpringResourceTemplateResolver automatically integrates with Spring's own
//        // resource resolution infrastructure, which is highly recommended.
//        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//        templateResolver.setApplicationContext(applicationContext);
//        templateResolver.setPrefix("classpath:/templates/");
//        templateResolver.setSuffix(".html");
//        // HTML is the default value, added here for the sake of clarity.
//        templateResolver.setTemplateMode(TemplateMode.HTML);
//        // Template cache is true by default. Set to false if you want
//        // templates to be automatically updated when modified.
//        templateResolver.setCacheable(true);
//        return templateResolver;
//    }
//
//    /**
//     * 引擎配置
//     *
//     * @author   chengxudong               chengxudong@microwu.com
//     * @date    2019/7/31  15:16
//     *
//     * @param
//     * @return  org.thymeleaf.spring5.SpringTemplateEngine
//     */
//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        // SpringTemplateEngine automatically applies SpringStandardDialect and
//        // enables Spring's own MessageSource message resolution mechanisms.
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver());
//        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
//        // speed up execution in most scenarios, but might be incompatible
//        // with specific cases when expressions in one template are reused
//        // across different data types, so this flag is "false" by default
//        // for safer backwards compatibility.
//        templateEngine.setEnableSpringELCompiler(true);
//        return templateEngine;
//    }
//
//    /**
//     *
//     *
//     * @author   chengxudong               chengxudong@microwu.com
//     * @date    2019/7/31  15:17
//     *
//     * @param
//     * @return  org.thymeleaf.spring5.view.ThymeleafViewResolver
//     */
//    @Bean
//    public ThymeleafViewResolver viewResolver() {
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(templateEngine());
//        return viewResolver;
//
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 增加 XML 消息转换器
//        Jackson2ObjectMapperBuilder xmlBuilder = Jackson2ObjectMapperBuilder.xml();
//        xmlBuilder.indentOutput(true);
//        converters.add(new MappingJackson2XmlHttpMessageConverter(xmlBuilder.build()));
//
//        // 还可以配置FastJson, 这里就不配置了
//    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
    }
}