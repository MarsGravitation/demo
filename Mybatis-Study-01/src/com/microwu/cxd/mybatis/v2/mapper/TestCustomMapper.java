package com.microwu.cxd.mybatis.v2.mapper;

import com.microwu.cxd.mybatis.v2.annotation.Pojo;
import com.microwu.cxd.mybatis.v2.annotation.Select;

@Pojo(User.class)
public interface TestCustomMapper {
    @Select(value = "select * from user where id = %d")
    public User selectOneByPrimaryKey(int id);
}
