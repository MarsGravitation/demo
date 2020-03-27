package com.microwu.cxd.domain;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/4   9:45
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class User {
    private Long id;
    private String userName;
    private String password;
    private List<Post> posts;
    private List<User> follows;
}