package com.microwu.cxd.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.io.File;
import java.util.*;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/12/29   9:44
 * Update History:
 * Author        Time            Content
 */
public class WriteTest {

    private static final String PATH = "D:\\data\\test\\excel\\";

    /**
     * 最简单的写
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 9:48
     *
     * @param:
     * @return: void
     */
    @Test
    public void simpleWrite() {
        // 注意 simpleWrite 在数量不大的情况下可以使用（5000 以内）
        // 数据量大参照 重复多次写入

        String fileName = PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要只能写用哪个 class 去写，然后写到第一个 sheet，名字为
        // 模板，然后文件流会自动关闭
        // 如果这里想使用 03，则传入 excelType 参数即可
        EasyExcel.write(fileName, DemoData.class)
                .sheet("模板")
                .doWrite(() -> data());

        // 写法 2
        fileName = PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, DemoData.class).sheet("模板")
                .doWrite(data());

        // 写法 3
        fileName = PATH + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里需要指定写用哪个 class 去写
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(data(), writeSheet);
        }
    }

    /**
     * 根据参数只导出指定列
     *
     * 1. 创建 excel 对应的实体对象
     * 2. 根据自己或者排除自己需要的列
     * 3. 直接写即可
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 10:39
     *
     * @param:
     * @return: void
     */
    @Test
    public void excludeOrIncludeWrite() {
        String fileName = PATH + "excludeOrIncludeWrite" + System.currentTimeMillis() + ".xlsx";

        // 这里需要注意，在使用 ExcelProperty 注解的使用，如果想不空列
        // 则需要加入 order 字段，而不是 index，order 会忽略空列，然后
        // 继续往后，而 index 不会忽略空列，在第几列就是第几列

        Set<String> excludeColumnFieldNames = new HashSet<>();
        excludeColumnFieldNames.add("date");
        EasyExcel.write(fileName, DemoData.class).excludeColumnFieldNames(excludeColumnFieldNames)
                .sheet("模板").doWrite(data());

        // includeColumnFieldNames 只导出某个字段
    }

    @Test
    public void indexWrite() {
        String fileName = PATH + "indexWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, IndexData.class).sheet("模板").doWrite(data());
    }

    /**
     * 重复多次写入
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 10:48
     *
     * @param:
     * @return: void
     */
    @Test
    public void repeatedWrite() {
        // 写法 1：如果写到同一个 sheet
        String fileName = PATH + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build()) {
            // 这里注意，如果同一个 sheet 只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            for (int i = 0; i < 5; i++) {
                // 分页去数据库查询数据，这里可以去数据库查询每一页的数据
                excelWriter.write(data(), writeSheet);
            }
        }

        // 方法2：如果写到不同的 sheet 同一个对象
        fileName = PATH + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 指定文件
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build()) {
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = data();
                excelWriter.write(data, writeSheet);
            }
        }

        // 方法3 如果写到不同的sheet 不同的对象
        fileName = PATH + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 指定文件
        try (ExcelWriter excelWriter = EasyExcel.write(fileName).build()) {
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class
                // 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = data();
                excelWriter.write(data, writeSheet);
            }
        }

    }

    @Test
    public void converterWrite() {
        String fileName = PATH + "converterWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, ConverterData.class).sheet("模板").doWrite(data());
    }

    /**
     * 图片导出
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 11:17
     *
     * @param:
     * @return: void
     */
    @Test
    public void imageWrite() {

    }

    /**
     * 超链接、备注、公式、指定单个单元格的样式、单个单元格多种样式
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 11:18
     *
     * @param:
     * @return: void
     */
    @Test
    public void writeCellDataWrite() {

    }

    /**
     * 根据模板写入
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:13
     *
     * @param:
     * @return: void
     */
    @Test
    public void templateWrite() {
        String templateFileName = PATH + "demo" + File.separator + "demo.xlsx";
        String fileName = PATH + "templateWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, DemoData.class).withTemplate(templateFileName).sheet().doWrite(data());
    }

    /**
     * 列宽，行高
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:13
     *
     * @param:
     * @return: void
     */
    @Test
    public void widthAndHeightWrite() {

    }

    /**
     * 注解形式自定义样式
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:34
     *
     * @param:
     * @return: void
     */
    @Test
    public void annotationStyleWrite() {
        String fileName = PATH + "annotationStyleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoStyleData.class).sheet("模板").doWrite(data());
    }

    /**
     * 拦截器形式自定义样式
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:44
     *
     * @param:
     * @return: void
     */
    @Test
    public void handlerStyleWrite() {
        // 方法 1: 使用已有的策略 推荐
        // HorizontalCellStyleStrategy 每一行的样式都一样 或者隔行一样
        // AbstractVerticalCellStyleStrategy 每一列的样式都一样 需要自己回调每一页
        String fileName = PATH + "handlerStyleWrite" + System.currentTimeMillis() + ".xlsx";
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 20);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为 FillPatternType.SOLID_FOREGROUND，不然无法显示背景颜色
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式，内容是内容的样式，其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        // 这里需要指定写用哪个 class 去写
        EasyExcel.write(fileName, DemoData.class)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .sheet("模板")
                .doWrite(data());
    }

    /**
     * 合并单元格
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:45
     *
     * @param:
     * @return: void
     */
    @Test
    public void mergeWrite() {

    }

    /**
     * 使用 table 去写
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:46
     *
     * @param:
     * @return: void
     */
    @Test
    public void tableWrite() {

    }

    /**
     * 动态头，实时生成头写入
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:46
     *
     * @param:
     * @return: void
     */
    @Test
    public void dynamicHeadWrite() {

    }

    /**
     * 自动列宽
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:49
     *
     * @param:
     * @return: void
     */
    @Test
    public void longestMatchColumnWidthWrite() {

    }

    /**
     * 下拉，超链接等自定义拦截器
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:50
     *
     * @param:
     * @return: void
     */
    @Test
    public void customHandlerWrite() {

    }

    /**
     * 插入批注
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:51
     *
     * @param:
     * @return: void
     */
    @Test
    public void commentWrite() {

    }

    /**
     * 可变标题处理
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:51
     *
     * @param:
     * @return: void
     */
    @Test
    public void variableTitleWrite() {

    }

    /**
     * 不创建对象的写
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 14:52
     *
     * @param:
     * @return: void
     */
    @Test
    public void noModelWrite() {

    }

    /**
     * 项目测试
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/12/29 15:14
     *
     * @param:
     * @return: void
     */
    @Test
    public void test() {
        String fileName = PATH + "pfic" + System.currentTimeMillis() + ".xlsx";
        // 生成十条错误数据
        List<FailureReason> failureReasons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FailureReason reason = new FailureReason();
            reason.setMobile("18435202728");
            reason.setReason("网关错误");
            reason.setRows(i);
            failureReasons.add(reason);
        }

        List<RepeatMobile> repeatMobiles = new ArrayList<>();
        for (int i = 0; i <  5; i++) {
            RepeatMobile rm = new RepeatMobile();
            rm.setMobile("18435202728");
            rm.setCount(i);
            repeatMobiles.add(rm);
        }

//        // 头的策略
//        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
//        // 背景设置为红色
//        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//        WriteFont headWriteFont = new WriteFont();
//        headWriteFont.setFontHeightInPoints((short) 20);
//        headWriteCellStyle.setWriteFont(headWriteFont);
//        // 内容的策略
//        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
//        // 这里需要指定 FillPatternType 为 FillPatternType.SOLID_FOREGROUND，不然无法显示背景颜色
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
//        // 背景绿色
//        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//        WriteFont contentWriteFont = new WriteFont();
//        // 字体大小
//        contentWriteFont.setFontHeightInPoints((short) 20);
//        contentWriteCellStyle.setWriteFont(contentWriteFont);
//        // 这个策略是 头是头的样式，内容是内容的样式，其他的策略可以自己实现
//        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
//
//        // 这里需要指定写用哪个 class 去写
//        EasyExcel.write(fileName, DemoData.class)
//                .registerWriteHandler(horizontalCellStyleStrategy)
//                .sheet("模板")
//                .doWrite(data());
        // 这里 指定文件
        try (ExcelWriter excelWriter = EasyExcel.write(fileName).build()) {
            // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "错误手机号及其位置").head(FailureReason.class).build();
            excelWriter.write(failureReasons, writeSheet);

            WriteSheet writeSheet2 = EasyExcel.writerSheet(1, "重复手机号").head(RepeatMobile.class).build();
            excelWriter.write(repeatMobiles, writeSheet2);
        }
    }

//    private List<LongestMatchColumnWidthData> dataLong() {
//        List<LongestMatchColumnWidthData> list = ListUtils.newArrayList();
//        for (int i = 0; i < 10; i++) {
//            LongestMatchColumnWidthData data = new LongestMatchColumnWidthData();
//            data.setString("测试很长的字符串测试很长的字符串测试很长的字符串" + i);
//            data.setDate(new Date());
//            data.setDoubleData(1000000000000.0);
//            list.add(data);
//        }
//        return list;
//    }

    private List<List<String>> variableTitleHead() {
        List<List<String>> list = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add("string" + System.currentTimeMillis());
        List<String> head1 = ListUtils.newArrayList();
        head1.add("number" + System.currentTimeMillis());
        List<String> head2 = ListUtils.newArrayList();
        head2.add("date" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<String>> head() {
        List<List<String>> list = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = ListUtils.newArrayList();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = ListUtils.newArrayList();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

}
