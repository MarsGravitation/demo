package com.microwu.cxd.netty.core;

import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/1   18:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class Address {

    private String ip;

    private int port;

    public String uniqueKey() {
        return this.ip + ":" + this.port;
    }
}