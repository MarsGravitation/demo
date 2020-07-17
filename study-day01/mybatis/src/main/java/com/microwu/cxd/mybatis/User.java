package com.microwu.cxd.mybatis;

import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/16   16:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private String mobile;

    private String role;
}