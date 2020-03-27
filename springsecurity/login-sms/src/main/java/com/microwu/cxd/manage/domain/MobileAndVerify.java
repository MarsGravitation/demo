package com.microwu.cxd.manage.domain;

import lombok.Data;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   16:52
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class MobileAndVerify {
    private String mobile;

    private String verifyCode;
}