package com.microwu.cxd.manage.domain;

import lombok.Data;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/16   14:42
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class UserDAO {
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String roles;
}