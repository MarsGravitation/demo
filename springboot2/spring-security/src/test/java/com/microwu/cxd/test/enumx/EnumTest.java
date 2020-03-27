package com.microwu.cxd.test.enumx;

import com.microwu.cxd.common.utils.enums.JwtRedisEnum;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   13:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EnumTest {
    public static void main(String[] args) {
        String tokenKey = JwtRedisEnum.JWT_TOKEN.getKey("cxd", "123456");
        System.out.println(tokenKey);

    }
}