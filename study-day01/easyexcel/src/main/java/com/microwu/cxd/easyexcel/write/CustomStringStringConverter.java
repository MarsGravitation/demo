package com.microwu.cxd.easyexcel.write;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * Description:
 *
 * https://juejin.cn/post/7171770606940061727#heading-8
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/12/29   11:10
 * Update History:
 * Author        Time            Content
 */
public class CustomStringStringConverter implements Converter<String> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 读的时候会调用
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 11:13
     *
     * @param: context - [com.alibaba.excel.converters.ReadConverterContext<?>]
     * @return: java.lang.String
     */
    @Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        return context.getReadCellData().getStringValue();
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) throws Exception {
        return new WriteCellData<>("自定义：" + context.getValue());
    }
}
