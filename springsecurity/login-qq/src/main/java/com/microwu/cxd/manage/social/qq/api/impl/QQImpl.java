package com.microwu.cxd.manage.social.qq.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.manage.social.qq.api.QQ;
import com.microwu.cxd.manage.social.qq.domain.QQUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/10/1   10:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {
    /**
     * 获取用户openid的URL
     */
    private static final String QQ_URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    /**
     * 获取用户信息的URL
     */
    private static final String QQ_URL_GET_USE_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
    /**
     * appId
     */
    private String appId;
    /**
     * openId 请求QQ_URL_GET_OPENID返回
     */
    private String openId;
    /**
     * 工具类
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(QQImpl.class);

    public QQImpl(String accessToken, String appId) {
        // accessToken作为查询参数来携带
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        String url = String.format(QQ_URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);

        logger.info("[QQImpl] QQ_URL_GET_OPENID = {}, result = {}", QQ_URL_GET_OPENID, result);

        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }
    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(QQ_URL_GET_USE_INFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);

        logger.info("result = {}", result);

        QQUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(result, QQUserInfo.class);
            userInfo.setOpenId(openId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}