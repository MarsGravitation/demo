package com.microwu.cxd.mybatis;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/6/16   16:31
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface UserMapper {
    User selectUser(Long id);
}
