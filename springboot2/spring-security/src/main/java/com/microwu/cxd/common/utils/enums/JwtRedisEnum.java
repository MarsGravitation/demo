package com.microwu.cxd.common.utils.enums;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   10:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public enum JwtRedisEnum {

    JWT_TOKEN("token:"),
    JWT_AUTHENTICATION("authentication:");

    private String prefix;

    private JwtRedisEnum(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 获取key
     *
     * @param username：xxx@163.com
     * @param randomKey：xxxxxx
     * @return
     */
    public static String getTokenKey(String username, String randomKey) {
        return JWT_TOKEN.prefix + username + ":" + randomKey;
    }

    public String getKey(String username, String randomKey) {
        return this.prefix + username + ":" + randomKey;
    }

    /**
     * 获取身份认证key
     *
     * @param username：用户名
     * @return
     */
    public static String getAuthenticationKey(String username, String randomKey) {
        return JWT_AUTHENTICATION.prefix + username + ":" + randomKey;
    }
}