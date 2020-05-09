package com.microwu.cxd.auth.security.mobile;

import com.microwu.cxd.auth.domain.MobileAuthenticationToken;
import com.microwu.cxd.auth.domain.UserVO;
import com.microwu.cxd.auth.service.UserService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/9   9:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        UserVO userVO = userService.findUserByMobile((String) mobileAuthenticationToken.getPrincipal());
        String[] authorityStrings = {"USER"};
        List<SimpleGrantedAuthority> authorities = Stream.of(authorityStrings).map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());

        return new MobileAuthenticationToken(userVO, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return MobileAuthenticationToken.class.isAssignableFrom(aClass);
    }

}