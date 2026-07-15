package com.microwu.cxd.easyexcel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;

import java.util.Date;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/12/29   11:04
 * Update History:
 * Author        Time            Content
 */
public class ConverterData {

    @ExcelProperty(value = "字符串标题", converter = CustomStringStringConverter.class)
    private String string;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("日期标题")
    private Date date;

    @NumberFormat("#.##%")
    @ExcelProperty("数字标题")
    private Double doubleData;

}
