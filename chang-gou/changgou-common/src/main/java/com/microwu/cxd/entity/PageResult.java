package com.microwu.cxd.entity;

import java.util.List;

/**
 * Description: 分页结果类
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/29   10:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PageResult<T> {

    private Long total;

    private List<T> rows;

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}