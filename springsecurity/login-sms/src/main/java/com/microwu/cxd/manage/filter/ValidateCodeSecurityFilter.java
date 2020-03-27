package com.microwu.cxd.manage.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.manage.utils.JsonUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   17:29
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ValidateCodeSecurityFilter extends OncePerRequestFilter {

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (pathMatcher.match(httpServletRequest.getRequestURI(), "/validate/code")) {
            // 1. 从request获取校验码
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject requestBody = JsonUtils.getRequestBody(httpServletRequest);
            String validateCode = requestBody.getString("validateCode");
            // 2. 从session 中获取校验码
            HttpSession session = httpServletRequest.getSession();
            String validateCodeSession = (String) session.getAttribute("validateCode");
            if (!validateCode.equals(validateCodeSession)) {
                throw new RuntimeException("验证码校验失败");
            }
            // 3. 校验成功, 删除校验码
            httpServletRequest.getSession().removeAttribute("validateCode");

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}