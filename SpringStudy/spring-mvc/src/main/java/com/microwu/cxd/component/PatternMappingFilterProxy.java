package com.microwu.cxd.component;


import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/31   15:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PatternMappingFilterProxy implements Filter {

    private final Filter delegate;

    private final List<String> pathUrlPatterns = new ArrayList<>();

    private PathMatcher pathMathcher;

    public PatternMappingFilterProxy(Filter delegate, String... urlPatterns) {
        this.delegate = delegate;
        int length = urlPatterns.length;
        pathMathcher = new AntPathMatcher();
        for (int index = 0; index < length; index++) {
            String urlPattern = urlPatterns[index];
            this.pathUrlPatterns.add(urlPattern);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String path = httpRequest.getRequestURI();
        if (this.matches(path)) {
            this.delegate.doFilter(servletRequest, servletResponse, filterChain);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean matches(String requestPath) {
        for (String pattern : pathUrlPatterns) {
            if (pathMathcher.match(pattern, requestPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.delegate.init(filterConfig);
    }

    @Override
    public void destroy() {
        this.delegate.destroy();
    }

    public List<String> getPathUrlPatterns() {
        return pathUrlPatterns;
    }

    public void setPathUrlPatterns(List<String> urlPatterns) {
        pathUrlPatterns.clear();
        pathUrlPatterns.addAll(urlPatterns);
    }
}