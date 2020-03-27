package com.microwu.cxd.common.utils.enums;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   10:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public enum  RedisKeyGenerator {

    CXD("cxd");

    private String prefix;

    private RedisKeyGenerator(String prefix) {
        this.prefix = prefix;
    }

    public String generateKey(Object... keys) {
        StringBuilder stringBuilder = new StringBuilder(prefix);
        for(Object key : keys) {
            stringBuilder.append(":")
                    .append(key);
        }
        return stringBuilder.toString();

    }

}