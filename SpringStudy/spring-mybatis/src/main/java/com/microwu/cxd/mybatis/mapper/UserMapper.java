package com.microwu.cxd.mybatis.mapper;

import com.microwu.cxd.mybatis.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/21   14:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{userId")
    User getUser(@Param("userId") String userId);

}