package com.microwu.cxd.manage.security;

import com.microwu.cxd.manage.domain.UserVO;
import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   10:24
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private UserVO userVO;

    public CustomAuthenticationToken(UserVO userVO) {
        // 未认证
        super(null);
        this.userVO = userVO;
        setAuthenticated(false);
    }

    public CustomAuthenticationToken(UserVO userVO, Collection<? extends GrantedAuthority> authorities) {
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