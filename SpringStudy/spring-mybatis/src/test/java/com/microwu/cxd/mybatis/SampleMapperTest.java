package com.microwu.cxd.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/21   16:24
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringJUnitConfig(locations = {""})
public class SampleMapperTest {

    @Configuration
    @ImportResource("classpath:org/mybatis/spring/sample/config/applicationContext-infrastructure.xml")
    @MapperScan("org.mybatis.spring.sample.mapper")
    static class AppConfig {
    }
}