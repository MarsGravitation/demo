package com.microwu.cxd.movie.config;

import feign.RequestTemplate;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/12   17:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class CustomerFeignClientInterceptor extends OAuth2FeignRequestInterceptor {

    private static final String Y = "y";

    private final OAuth2ClientContext oAuth2ClientContext;
    private final AccessTokenContextRelay accessTokenContextRelay;

    /**
     * Default constructor which uses the provided OAuth2ClientContext and Bearer tokens
     * within Authorization header
     *
     * @param oAuth2ClientContext     provided context
     * @param resource                type of resource to be accessed
     * @param
     */
    public CustomerFeignClientInterceptor(OAuth2ClientContext oAuth2ClientContext
            , OAuth2ProtectedResourceDetails resource) {
        super(oAuth2ClientContext, resource);
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.accessTokenContextRelay = new AccessTokenContextRelay(oAuth2ClientContext);
    }


    /**
     * Create a template with the header of provided name and extracted extract
     * 1. 如果使用 非web 请求，header 区别
     * 2. 根据authentication 还原请求token
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        Collection<String> fromHeader = template.headers().get("from");
        if (!CollectionUtils.isEmpty(fromHeader) && fromHeader.contains(Y)) {
            return;
        }

        /**
         * 关键代码：
         * oAuth2ClientContext.getAccessToken() 如果没有，意味着这个不需要权限，
         * 也就不需要将 token 传递过去
         */
        accessTokenContextRelay.copyToken();
        if (oAuth2ClientContext != null
                && oAuth2ClientContext.getAccessToken() != null) {
            super.apply(template);
        }
    }
}