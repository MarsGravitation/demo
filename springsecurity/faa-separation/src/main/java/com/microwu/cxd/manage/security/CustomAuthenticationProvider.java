package com.microwu.cxd.manage.security;

import com.microwu.cxd.manage.domain.Permission;
import com.microwu.cxd.manage.domain.UserDO;
import com.microwu.cxd.manage.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:     https://www.jianshu.com/p/693914564406
 *
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   10:33
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public class CustomAuthenticationProvider implements AuthenticationProvider {
    private UserService userService;

    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomJsonAuthenticationToken authenticationToken = (CustomJsonAuthenticationToken) authentication;
        UserDO userDO = (UserDO) authenticationToken.getPrincipal();
        UserDO result = userService.getUsername(userDO.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(userDO.getPassword(), result.getPassword())) {
            return null;
        }else {
            List<Permission> permissionList = userService.getPermissions(result.getId());
            Set<SimpleGrantedAuthority> grantedAuthorities = permissionList.parallelStream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
                    .map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
            return new CustomJsonAuthenticationToken(result, grantedAuthorities);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 只支持CustomJsonAuthenticationToken
        return (authentication.isAssignableFrom(CustomJsonAuthenticationToken.class));
    }
}