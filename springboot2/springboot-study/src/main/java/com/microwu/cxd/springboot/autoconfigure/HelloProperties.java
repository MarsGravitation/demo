package com.microwu.cxd.springboot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * @ConfigurationProperties 标注的 Class 需要在配置类上使用 @EnableConfigurationProperties 进行启用。
 * @EnableConfigurationProperties 指定的配置类会自动注册为 Spring bean 容器中的一个 bean，然后可以在配置类中自动注入对应的属性类
 *
 * @ConfigurationProperties SpringBoot 会从 Environment 中获取器属性对应的属性给其注入
 * 通常是跟 @Configuration 标注的 Class 一起使用，其内部会诸如 @ConfigurationProperties 标注的对象用来定义 bean
 *
 * 当 @ConfigurationProperties 标注的 Class 本身就为一个 bean 时就不需要 @EnableConfigurationProperties 指定了
 *
 * @Configuration
 * @EnableConfigurationProperties(TestConfigurationProperties.class)
 * public class TestConfig {
 *
 *     @Autowired
 *     private TestConfigurationProperties props;
 *
 *     @Bean
 *     public Object initBean() {
 *         //使用注入的ConfigurationProperties标注的对象进行bean构造
 *         return this.props.getAppName();
 *     }
 *
 * }
 *
 * @Configuration  + @EnableConfigurationProperties SpringBoot 会把它实例化为一个 bean
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/22   14:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
//@Configuration
@ConfigurationProperties("autoconfigure.hello")
public class HelloProperties {

    private String name;

    private String message;

    // 级联绑定
    private Inner inner;

    @Data
    static class Inner {
        private String username;
        private String password;
    }

    /**
     * test.config.list[0]=ABC
     * test.config.list[1]=DEF
     * test.config.list[2]=GHI
     *
     * test.config.list=ABC,DEF,GHI
     *
     * test.config.list:
     *   - ABC
     *   - DEF
     *   - GHI
     */
    private List<String> list;

    /**
     * test.config.inners[0].username=u1
     * test.config.inners[0].password=p1
     *  test.config.inners[1].username=u2
     * test.config.inners[1].password=p2
     *
     *  test.config.inners:
     *   -
     *     username: u1
     *     password: p1
     *   -
     *     username: u2
     *     password: p2
     */
    private List<Inner> inners;

    /**
     * test.config.map.key1.username=u1
     * test.config.map.key1.password=p1
     *
     * test.config.map.key2.username=u2
     * test.config.map.key2.password=p2
     *
     * test.config.map:
     *   key1:
     *     username: u1
     *     password: p1
     *   key2:
     *     username: u2
     *     password: p2
     */
    private Map<String, String> map;

}