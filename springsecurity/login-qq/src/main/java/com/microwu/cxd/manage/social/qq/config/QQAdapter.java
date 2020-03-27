package com.microwu.cxd.manage.social.qq.config;

import com.microwu.cxd.manage.social.qq.api.QQ;
import com.microwu.cxd.manage.social.qq.domain.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/10/1   16:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QQAdapter implements ApiAdapter<QQ> {
    @Override
    public boolean test(QQ qq) {
        return true;
    }

    @Override
    public void setConnectionValues(QQ qq, ConnectionValues connectionValues) {
        QQUserInfo userInfo = qq.getUserInfo();

        connectionValues.setProviderUserId(userInfo.getOpenId());;
        connectionValues.setDisplayName(userInfo.getNickname());;
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());;
        connectionValues.setProfileUrl(null);
    }

    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        return null;
    }

    @Override
    public void updateStatus(QQ qq, String s) {

    }
}