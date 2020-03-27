package com.microwu.cxd.manage.security;

import com.microwu.cxd.manage.domain.UserVO;
import com.microwu.cxd.manage.service.UserService;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   10:23
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Setter
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserVO userVO = (UserVO) authentication.getPrincipal();
        UserVO authenticationUser = userService.getUser(userVO);
        if(authentication != null) {
            return new CustomAuthenticationToken(authenticationUser, Arrays.asList(new SimpleGrantedAuthority("USER")));
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}