package com.microwu.cxd.dubbo.service;

import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/26   14:13
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface RestService {

    String param(String param);

    String params(int a, String b);

    String headers(String header, String header2, Integer param);

    String pathVariables(String path1, String path2, String param);

    String form(String form);

    User requestBodyMap(Map<String, Object> data, String param);

    Map<String, Object> requestBodyUser(User user);
}