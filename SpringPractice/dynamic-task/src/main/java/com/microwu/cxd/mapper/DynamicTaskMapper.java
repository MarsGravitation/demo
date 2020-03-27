package com.microwu.cxd.mapper;

import com.microwu.cxd.common.domain.TaskJobDO;

import java.util.List;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2020/2/9   20:57
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface DynamicTaskMapper {

    Integer insert(TaskJobDO taskJobDO);

    List<TaskJobDO> list();
}
