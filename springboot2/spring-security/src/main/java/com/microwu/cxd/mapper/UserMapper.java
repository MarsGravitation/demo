package com.microwu.cxd.mapper;

import com.microwu.cxd.domain.Permission;
import com.microwu.cxd.domain.User;

import java.util.List;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2020/1/13   15:36
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface UserMapper {

    User getByUsername(String username);

    List<Permission> getPermissions(Long id);

    User getByMobile(String mobile);
}
