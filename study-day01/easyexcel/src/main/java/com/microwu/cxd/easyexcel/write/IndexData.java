package com.microwu.cxd.easyexcel.write;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/12/29   10:46
 * Update History:
 * Author        Time            Content
 */
public class IndexData {

    @ExcelProperty(value = "字符串标题", index = 0)
    private String string;
    @ExcelProperty(value = "日期标题", index = 1)
    private Date date;
    /**
     * 这里设置3 会导致第二列空的
     */
    @ExcelProperty(value = "数字标题", index = 3)
    private Double doubleData;

}
