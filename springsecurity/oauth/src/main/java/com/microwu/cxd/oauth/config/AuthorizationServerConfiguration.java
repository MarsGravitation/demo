package com.microwu.cxd.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * Description: 资源服务器
 *  这里将客户端信息放到了内存中，生产中可以配置到数据库中。
 *  token的存储一般选择redis，一是性能比较好，二是自动过期，符合token的特性
 *
 *  Spring Security Oauth2的认证思路：
 *      > client 模式，没有用户的概念，直接与认证服务器交互，用配置中的客户端信息去申请accessToken，
 *      客户端由自己的client_id，client_secret 对应于用户的username，password，而客户端也有自己的authorities，
 *      采用client 模式认证时，对应的权限也就是客户端自己的authorities
 *
 *      > password模式，自身有一套用户体系，认证时需要带上自己的用户名和密码，以及客户端的client_id,client_secret。
 *      此时accessToken所包含的权限使用户本身的权限，而不是客户端的权限
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/5/5   17:16
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "order";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置两个客户端，一个用于password认证，一个用于client 认证
        clients.inMemory().withClient("client_1")
                .resourceIds(DEMO_RESOURCE_ID)
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("select")
                .authorities("client")
                .secret(passwordEncoder.encode("123456"))
                .and()
                .withClient("client_2")
                .resourceIds(DEMO_RESOURCE_ID)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("select")
                .authorities("client")
                .secret(passwordEncoder.encode("123456"));

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(new InMemoryTokenStore())
                .authenticationManager(authenticationManager);
    }

    /**
     * https://blog.csdn.net/u012040869/article/details/80140515
     *
     * 401 错误
     * 这个如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
     *
     * 如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的，走basic认证保护
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/5  18:12
     *
     * @param
     * @return  
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证
        security.allowFormAuthenticationForClients();
    }
}