package com.microwu.cxd.manage.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/18   15:40
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class IpAuthenticationToken extends AbstractAuthenticationToken {
    private String ip;

    public IpAuthenticationToken(String ip) {
        super(null);
        this.ip = ip;
        // 认证前的构造方法, 只需要IP地址, 需要传递给认证器, 只有IP
        // 不需要认证
        super.setAuthenticated(false);
    }

    public IpAuthenticationToken(String ip, Collection<? extends GrantedAuthority> authorities) {
        // 此构造方法用于认证成功, 封转用于认证信息, 权限也需要
        super(authorities);
        this.ip = ip;
        super.setAuthenticated(true);

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.ip;
    }

}