package com.microwu.cxd.security.json;

import com.microwu.cxd.domain.Permission;
import com.microwu.cxd.domain.User;
import com.microwu.cxd.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   9:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JsonAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public JsonAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = (User) authentication.getPrincipal();
        User result = userService.getByUsername(user.getUsername());
        if(passwordEncoder.matches(user.getPassword(), result.getPassword())) {
            List<Permission> permissionList = userService.getPermissions(result.getId());
            Set<SimpleGrantedAuthority> grantedAuthorities = permissionList.parallelStream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
                    .map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
            return new JsonAuthenticationToken(result, grantedAuthorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(JsonAuthenticationToken.class);
    }
}