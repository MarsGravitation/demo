package com.microwu.cxd.security.jwt;

import com.alibaba.fastjson.JSON;
import com.microwu.cxd.common.utils.JwtTokenUtil;
import com.microwu.cxd.common.utils.enums.JwtRedisEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * JWT过滤器
 *
 * @author chentongwei@bshf360.com 2018-06-08 14:31
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("请求路径：【{}】，请求方式为：【{}】", request.getRequestURI(), request.getMethod());

        if (antPathMatcher.match("/favicon.ico", request.getRequestURI())) {
            logger.info("jwt不拦截此路径：【{}】，请求方式为：【{}】", request.getRequestURI(), request.getMethod());
            filterChain.doFilter(request, response);
            return;
        }

        // 排除路径，并且如果是options请求是cors跨域预请求，设置allow对应头信息
        String[] permitUrls = getPermitUrls();
        for (int i = 0, length = permitUrls.length; i < length; i ++) {
            if (antPathMatcher.match(permitUrls[i], request.getRequestURI())
                    || Objects.equals(RequestMethod.OPTIONS.toString(), request.getMethod())) {
                logger.info("jwt不拦截此路径：【{}】，请求方式为：【{}】", request.getRequestURI(), request.getMethod());
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 获取Authorization
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authHeader) || ! authHeader.startsWith("Bearer ")) {
            logger.error("Authorization的开头不是Bearer，Authorization===>【{}】", authHeader);
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "暂无权限！");
            return;
        }

        // 截取token
        String authToken = authHeader.substring("Bearer ".length());

        // 判断token是否失效
        if (JwtTokenUtil.isTokenExpired(authToken)) {
            logger.info("token已过期！");
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "token已过期！");
            return;
        }

        String randomKey = JwtTokenUtil.getMd5KeyFromToken(authToken);
        String username = JwtTokenUtil.getUsernameFromToken(authToken);

        // 验证token是否合法
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(randomKey)) {
            logger.info("username【{}】或randomKey【{}】可能为null！", username, randomKey);
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "暂无权限！");
            return;
        }

        // 验证token是否存在（过期了也会消失）
        Object token = redisTemplate.opsForValue().get(JwtRedisEnum.getTokenKey(username, randomKey));
        if (Objects.isNull(token)) {
            logger.info("Redis里没查到key【{}】对应的value！", JwtRedisEnum.getTokenKey( username, randomKey));
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "token已过期！");
            return;
        }

        // 判断传来的token和存到redis的token是否一致
        if (! Objects.equals(token.toString(), authToken)) {
            logger.error("前端传来的token【{}】和redis里的token【{}】不一致！", authToken, token.toString());
            responseEntity(response, HttpStatus.UNAUTHORIZED.value(), "暂无权限！");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authenticationStr = redisTemplate.opsForValue().get(JwtRedisEnum.getAuthenticationKey(username, randomKey));
            Authentication authentication = JSON.parseObject(authenticationStr, Authentication.class);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // token过期时间
        long tokenExpireTime = JwtTokenUtil.getExpirationDateFromToken(authToken).getTime();
        // token还剩余多少时间过期
        long surplusExpireTime = (tokenExpireTime - System.currentTimeMillis()) / 1000;
        logger.info("surplusExpireTime:" + surplusExpireTime);

        filterChain.doFilter(request, response);
    }

    private String[] getPermitUrls() {

        String[] permitUrls = new String[] {"/", "/register", "/login", "/image", "/sms", "/mobile"};
        return permitUrls;
    }

    private void responseEntity(HttpServletResponse response, Integer status, String msg) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        try {
            response.getWriter().write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
