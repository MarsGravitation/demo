package com.microwu.cxd.manage.security;

import com.alibaba.fastjson.JSON;
import com.microwu.cxd.manage.bean.WebResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Description:     未登录状态下访问触发
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   14:21
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CustomAuthneticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        WebResponse webResponse = new WebResponse();
        webResponse.setCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        webResponse.setMessage("authenticate fail");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        httpServletResponse.getWriter().write(JSON.toJSONString(webResponse));
    }
}