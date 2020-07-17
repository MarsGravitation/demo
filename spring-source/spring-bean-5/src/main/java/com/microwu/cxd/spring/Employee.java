package com.microwu.cxd.spring;

import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   10:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class Employee {

    /**
     * 员工组名
     */
    private String group;

    /**
     * 是否配电话
     */
    private Boolean usesDialUp;

    /**
     * 部门
     */
    private String department;

    /**
     * 经理
     */
    private Employee manager;
}