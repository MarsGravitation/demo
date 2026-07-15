package com.microwu.cxd.mybatis.mapper;

import com.microwu.cxd.mybatis.NewMyBatisApplication;
import com.microwu.cxd.mybatis.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/26   8:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewMyBatisApplication.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

}
