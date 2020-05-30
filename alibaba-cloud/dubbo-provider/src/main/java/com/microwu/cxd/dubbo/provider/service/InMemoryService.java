package com.microwu.cxd.dubbo.provider.service;

import com.microwu.cxd.dubbo.service.User;
import com.microwu.cxd.dubbo.service.UserService;
import org.apache.dubbo.config.annotation.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/28   14:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class InMemoryService implements UserService {

    private Map<Long, User> map = new HashMap();

    @Override
    public boolean save(User user) {
        map.put(user.getId(), user);
        return true;
    }

    @Override
    public boolean remove(Long id) {
        map.remove(id);
        return true;
    }

    @Override
    public Collection<User> findAll() {
        return map.values();
    }
}