package com.microwu.cxd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   21:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@AllArgsConstructor
public class Order {
    private Long id;

    private String name;

    private String content;
}