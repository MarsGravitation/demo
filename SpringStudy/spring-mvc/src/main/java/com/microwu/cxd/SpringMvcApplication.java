package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author
 *
 * SpringBoot的默认配置
 *      只要我们将Thymeleaf依赖导入, 即启用了SpringBoot的自动配置
 *      当应用运行时, SpringBoot将会检测到类路径中的Thymeleaf, 然后自动配置视图解析器, 模版解析器以及模版引擎
 *
 *      如果什么都不配置, SpringBoot会默认查找类根目录下的templates文件夹下的模板
 *
 *      默认静态资源映射: META-INF/resources resources static public
 *
 */
@SpringBootApplication
@EnableRetry
@EnableAsync
public class SpringMvcApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(SpringMvcApplication.class, args);

    }
}
