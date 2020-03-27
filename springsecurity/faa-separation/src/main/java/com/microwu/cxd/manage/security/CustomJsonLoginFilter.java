package com.microwu.cxd.manage.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.manage.domain.UserDO;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:     继承 UsernamePasswordAuthenticationFilter, 重写 attemptAuthentication 方法
 *
 *     @Override
 *     public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
 *         JSONObject requestBody = getRequestBody(httpServletRequest);
 *         String username = requestBody.getString("username");
 *         String password = requestBody.getString("password");
 *         UserDO userDO = new UserDO(null, username, password, null);
 *         // 交给内部的manager, 进行解耦
 *         return super.getAuthenticationManager().authenticate(new CustomJsonAuthenticationToken(userDO));
 *     }
 *
 *     private JSONObject getRequestBody(HttpServletRequest request) {
 *         // 获取请求体
 *         StringBuilder stringBuilder = new StringBuilder();
 *         try {
 *             ServletInputStream inputStream = request.getInputStream();
 *             byte[] bytes = new byte[1024];
 *             int len;
 *             while((len = inputStream.read(bytes)) != -1) {
 *                 stringBuilder.append(new String(bytes, 0, len));
 *             }
 *             JSONObject jsonObject = JSON.parseObject(stringBuilder.toString());
 *             return jsonObject;
 *         } catch (IOException e) {
 *             e.printStackTrace();
 *             return null;
 *         }
 *     }
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   10:12
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CustomJsonLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)  || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            ObjectMapper objectMapper = new ObjectMapper();
            CustomJsonAuthenticationToken token = null;
            try {
                UserDO userDO = objectMapper.readValue(request.getInputStream(), UserDO.class);
                token = new CustomJsonAuthenticationToken(userDO);
            } catch (IOException e) {
                e.printStackTrace();
                token = new CustomJsonAuthenticationToken(null);
            } finally {
                return this.getAuthenticationManager().authenticate(token);
            }
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}