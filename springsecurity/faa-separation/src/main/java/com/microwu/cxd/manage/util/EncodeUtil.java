package com.microwu.cxd.manage.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   15:03
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EncodeUtil {
    public static String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}