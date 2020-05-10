package com.microwu.cxd.auth.mapper;

import com.microwu.cxd.auth.domain.UserVO;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/5/10   9:27
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */

public interface UserMapper {

    UserVO getById(Long id);

    UserVO getByUsername(String username);
}