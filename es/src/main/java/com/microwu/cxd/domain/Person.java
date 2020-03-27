package com.microwu.cxd.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/8   16:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class Person {

    @Id
    private String id;

    private String username;

    private String password;
}