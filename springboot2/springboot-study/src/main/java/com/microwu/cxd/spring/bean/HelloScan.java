package com.microwu.cxd.spring.bean;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * public class HelloImportSelector implements ImportSelector {
 *
 *     @Override
 *     public String[] selectImports(AnnotationMetadata importingClassMetadata) {
 *         Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(HelloServiceScan.class.getName());
 *         String[] basePackages = (String[]) annotationAttributes.get("basePackages");
 *         if (basePackages == null || basePackages.length == 0) {//HelloServiceScan的basePackages默认为空数组
 *             String basePackage = null;
 *             try {
 *                 basePackage = Class.forName(importingClassMetadata.getClassName()).getPackage().getName();
 *             } catch (ClassNotFoundException e) {
 *                 e.printStackTrace();
 *             }
 *             basePackages = new String[] {basePackage};
 *         }
 *         ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
 *         TypeFilter helloServiceFilter = new AssignableTypeFilter(HelloService.class);
 *         scanner.addIncludeFilter(helloServiceFilter);
 *         Set<String> classes = new HashSet<>();
 *         for (String basePackage : basePackages) {
 *             scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> classes.add(beanDefinition.getBeanClassName()));
 *         }
 *         return classes.toArray(new String[classes.size()]);
 *     }
 *
 * }
 * 在@Configuration配置类上就可以为@HelloServiceScan指定额外的basePackages属性了。
 *
 * @Configuration
 * @HelloServiceScan("com.elim.spring.core.importselector")
 * public class HelloConfiguration {
 *
 * }
 *
 * public class HelloImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
 *
 *     @Override
 *     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
 *         Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(HelloScan.class.getName());
 *         String[] basePackages = (String[]) annotationAttributes.get("basePackages");
 *         if (basePackages == null || basePackages.length == 0) {//HelloScan的basePackages默认为空数组
 *             String basePackage = null;
 *             try {
 *                 basePackage = Class.forName(importingClassMetadata.getClassName()).getPackage().getName();
 *             } catch (ClassNotFoundException e) {
 *                 e.printStackTrace();
 *             }
 *             basePackages = new String[] {basePackage};
 *         }
 *
 *         ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
 *         TypeFilter helloServiceFilter = new AssignableTypeFilter(HelloService.class);
 *         scanner.addIncludeFilter(helloServiceFilter);
 *         scanner.scan(basePackages);
 *     }
 *
 * }
 * 此时我们的HelloConfiguration可以定义为如下这样，它的效果和之前是一模一样的。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HelloImportSelector.class)
public @interface HelloScan {

    @AliasFor("value")
    String[] basePackages() default {};

    @AliasFor("basePackages")
    String[] value() default {};

}
