package com.microwu.cxd.mybatis.mapper;

import com.microwu.cxd.mybatis.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Description:
 *
 * - @Repository: 用于标记是数据访问 Bean 对象。在 MyBatis 的接口中，实际
 * 非必须，只是为了避免在 Service 中注入时无需报警
 *
 * - @Param: 声明变量名
 *  |- 单参数，非必须
 *  |- 方法为多参数时，必须
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/26   8:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Repository
public interface UserMapper {

    User selectById(Long id);

}
