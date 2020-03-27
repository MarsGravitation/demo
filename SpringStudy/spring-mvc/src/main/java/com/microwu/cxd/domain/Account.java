package com.microwu.cxd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:           2019/7/31   13:54
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String account;
    private String password;
    private String accountType;
    private String tel;
}