package com.microwu.cxd.dynamic.proxy;

/**
 * Description: 目标类
 * Author:   Administration
 * Date:     2019/2/27 13:42
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class UserServiceImpl implements UserService {
    @Override
    public String execute() {
        return "执行方法execute()";
    }
}