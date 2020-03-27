package com.microwu.cxd.manage.mapper;

import com.microwu.cxd.manage.domain.Permission;
import com.microwu.cxd.manage.domain.UserDO;

import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   10:06
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface UserMapper {
    Integer insert(UserDO userDO);

    UserDO getUsername(String username);

    List<Permission> getPermissions(Long id);
}