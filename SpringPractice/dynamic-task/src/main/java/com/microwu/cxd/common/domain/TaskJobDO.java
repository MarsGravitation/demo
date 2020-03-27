package com.microwu.cxd.common.domain;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/9   20:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class TaskJobDO {
    private Long id;

    private String beanName;

    private String methodName;

    private String methodParams;

    private String cronExpression;

    private String remake;

    private Integer status;

    private Date gmtCreate;

    private Date gmtModify;
}