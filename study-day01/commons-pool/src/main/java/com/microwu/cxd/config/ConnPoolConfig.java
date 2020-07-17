package com.microwu.cxd.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Description: 支持个性化配置
 *  这里继承是为了进行个性化配置，其他配置使用自定义配置
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   15:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConnPoolConfig extends GenericObjectPoolConfig {

    public ConnPoolConfig() {
        setMinIdle(5);
        setTestOnBorrow(true);
    }
}