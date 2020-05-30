package com.microwu.cxd.dubbo.service;

import java.util.Collection;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/5/26   14:18
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface UserService {

    boolean save(User user);

    boolean remove(Long id);

    Collection<User> findAll();
}
