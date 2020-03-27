package com.microwu.cxd.manage.security;

import com.microwu.cxd.manage.domain.UserDO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   10:27
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CustomJsonAuthenticationToken extends AbstractAuthenticationToken {
    private UserDO userDO;

    public CustomJsonAuthenticationToken(UserDO userDO) {
        super(null);
        this.userDO = userDO;
        super.setAuthenticated(false);
    }

    public CustomJsonAuthenticationToken(UserDO userDO, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userDO = userDO;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userDO;
    }
}