package com.microwu.cxd.mybatis.v2;

import com.microwu.cxd.mybatis.v2.mapper.TestCustomMapper;
import com.microwu.cxd.mybatis.v2.mapper.User;
import com.microwu.cxd.mybatis.v2.session.CustomSqlSession;
import com.microwu.cxd.mybatis.v2.session.SqlSessionFactory;

/**
 * Description: 测试类
 * Author:   Administration
 * Date:     2019/3/1 14:40
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class TestMybatis {
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        SqlSessionFactory factory = new SqlSessionFactory();
        //没有插件且没有开启缓存
        CustomSqlSession sqlSession = factory.build("com.microwu.cxd.mybatis.v2.mapper").openSession();
        TestCustomMapper mapper = sqlSession.getMapper(TestCustomMapper.class);
        User user = mapper.selectOneByPrimaryKey(1);
        System.out.println("第一次查询结果为: " + user);
        user = mapper.selectOneByPrimaryKey(1);
        System.out.println("第二次查询结果为: " + user);
    }
}