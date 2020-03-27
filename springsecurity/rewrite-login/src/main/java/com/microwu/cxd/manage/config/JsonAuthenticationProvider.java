package com.microwu.cxd.manage.config;

import com.microwu.cxd.manage.domain.UserVO;
import com.microwu.cxd.manage.service.UserService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/23   17:46
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class JsonAuthenticationProvider implements AuthenticationProvider {
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserVO userVO = (UserVO) authentication.getPrincipal();
        UserVO authenticationUserVO = userService.getUserVO(userVO);
        if(authenticationUserVO == null) {
            return null;
        }
        return new JsonAuthenticationToken(authenticationUserVO, Arrays.asList(new SimpleGrantedAuthority("USER")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (authentication.isAssignableFrom(JsonAuthenticationToken.class));
    }
}