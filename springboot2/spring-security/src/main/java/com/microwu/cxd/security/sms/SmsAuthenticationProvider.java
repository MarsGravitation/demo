package com.microwu.cxd.security.sms;

import com.microwu.cxd.domain.Permission;
import com.microwu.cxd.domain.User;
import com.microwu.cxd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/15   10:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.getByMobile(((User) authentication.getPrincipal()).getMobile());
        if(user != null) {
            List<Permission> permissionList = userService.getPermissions(user.getId());
            Set<SimpleGrantedAuthority> grantedAuthorities = permissionList.parallelStream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
                    .map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
            return new SmsAuthenticationToken(user, grantedAuthorities);
        }
        throw new AuthenticationServiceException("手机号不存在");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SmsAuthenticationToken.class);
    }
}