package com.microwu.cxd.reflex;

/**
 * Description:     Bean
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/20   10:19
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class User {
    /**
     * id
     */
    private Long id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户类型
     */
    private Integer userType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", userType=" + userType +
                '}';
    }

}