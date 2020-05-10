package com.microwu.cxd.auth.service;

import com.microwu.cxd.auth.domain.UserVO;
import com.microwu.cxd.auth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/9   10:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserVO findUserByMobile(String mobile) {
        UserVO userVO = new UserVO();
        userVO.setId(1L);
        userVO.setUsername("cxd");
        userVO.setPassword("123456");
        userVO.setMobile(mobile);
        return userVO;
    }

    public UserVO getById(Long id) {
        return userMapper.getById(id);
    }
}