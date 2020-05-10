package com.microwu.cxd.auth.domain;

import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/9   10:02
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class UserVO {

    private Long id;

    private String username;

    private String password;

    private String mobile;

    private String role;

}