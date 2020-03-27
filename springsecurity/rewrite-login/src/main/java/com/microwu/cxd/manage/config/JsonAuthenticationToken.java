package com.microwu.cxd.manage.config;

import com.microwu.cxd.manage.domain.UserVO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/23   17:44
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JsonAuthenticationToken extends AbstractAuthenticationToken {
    private UserVO userVO;

    public JsonAuthenticationToken(UserVO userVO) {
        super(null);
        this.userVO = userVO;
        setAuthenticated(false);
    }

    public JsonAuthenticationToken(UserVO userVO, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userVO = userVO;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userVO;
    }
}