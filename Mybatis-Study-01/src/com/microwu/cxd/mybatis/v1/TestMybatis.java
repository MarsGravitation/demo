package com.microwu.cxd.mybatis.v1;

/**
 * Description: 测试类
 * Author:   Administration
 * Date:     2019/2/27 9:23
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class TestMybatis {
    public static void main(String[] args) {
        CustomSqlSession customSqlSession = new CustomSqlSession(new CustomConfiguration(), new CustomDefaultExecutor());
        TestCustomMapper testCustomMapper = customSqlSession.getMapper(TestCustomMapper.class);
        System.out.println(testCustomMapper.selectByPrimaryByKey(1));
    }
}