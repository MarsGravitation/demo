package com.microwu.cxd.manage.mapper;

import com.microwu.cxd.manage.domain.UserDAO;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/8/17   11:27
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface UserMapper {
    UserDAO getByUsername(String username);

    Integer insertUser(UserDAO userDAO);
}
