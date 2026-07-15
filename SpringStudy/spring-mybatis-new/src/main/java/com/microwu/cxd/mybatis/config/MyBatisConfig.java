package com.microwu.cxd.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/26   8:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@MapperScan(basePackages = "com.microwu.cxd.mybatis.mapper")
public class MyBatisConfig {
}
