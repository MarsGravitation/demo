package com.microwu.cxd.manage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   10:04
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDO {
    private Long id;

    private String username;

    private String password;

    private String mobile;

    private String nickname;
}