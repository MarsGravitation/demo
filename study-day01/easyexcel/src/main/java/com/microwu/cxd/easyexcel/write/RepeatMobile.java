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
 * Date:       2022/12/29   15:13
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
public class RepeatMobile {

    @ColumnWidth(20)
    @ExcelProperty("重复手机号")
    private String mobile;

    @ColumnWidth(30)
    @ExcelProperty("因重复未发送的数量")
    private Integer count;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
