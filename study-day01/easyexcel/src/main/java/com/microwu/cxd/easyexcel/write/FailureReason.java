package com.microwu.cxd.easyexcel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/12/29   15:12
 * Update History:
 * Author        Time            Content
 */
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 22,
        horizontalAlignment = HorizontalAlignmentEnum.CENTER_SELECTION,
        verticalAlignment = VerticalAlignmentEnum.CENTER)
@HeadFontStyle(fontName = "仿宋", fontHeightInPoints = 18)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER_SELECTION,
        verticalAlignment = VerticalAlignmentEnum.CENTER)
@ContentFontStyle(fontHeightInPoints = 12)
//@ContentRowHeight(19 * 20)
//@HeadRowHeight(19 * 20)
public class FailureReason {

    /**
     * 手机号
     */
    @ColumnWidth(20)
    @ExcelProperty("手机号")
    private String mobile;

    /**
     * 失败原因
     */
    @ColumnWidth(20)
    @ExcelProperty("错误类型")
    private String reason;

    /**
     * 行数
     */
    @ColumnWidth(20)
    @ExcelProperty("错误行数")
    private Integer rows;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
