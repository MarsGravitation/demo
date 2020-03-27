package com.microwu.cxd.manage.provider;

import com.microwu.cxd.manage.token.IpAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/19   11:10
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class IpAuthenticationProvider implements AuthenticationProvider {
    final static Map<String, SimpleGrantedAuthority> ipAuthorityMap = new ConcurrentHashMap<>();

    static {
        // 维护一个IP白名单列表, 每个IP对应一定的权限
        ipAuthorityMap.put("127.0.0.1", new SimpleGrantedAuthority("ADMIN"));
        ipAuthorityMap.put("10.0.0.10", new SimpleGrantedAuthority("ADMIN"));
        ipAuthorityMap.put("10.0.0.11", new SimpleGrantedAuthority("FRIEND"));
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        IpAuthenticationToken ipAuthenticationToken = (IpAuthenticationToken) authentication;
        String ip = ipAuthenticationToken.getIp();
        SimpleGrantedAuthority simpleGrantedAuthority = ipAuthorityMap.get(ip);
        if(simpleGrantedAuthority == null) {
            // 不在白名单列表中
            return null;
        } else {
            // 封转权限信息, 并此事身份已经被认证
            return new IpAuthenticationToken(ip, Arrays.asList(simpleGrantedAuthority));
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 只支持IpAuthenticationToken 该身份
        return (IpAuthenticationToken.class.isAssignableFrom(authentication));
    }
}