package com.cxd.hbase;

import com.cxd.hbase.utils.HBaseConnect;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/5   15:34
 * Update History:
 * Author        Time            Content
 */
public class HBaseDemoApplication {

    public static void main(String[] args) throws IOException {
//        getCells("bigdata", "student", "1001");
        scanRows("bigdata", "student", "1001", "1003");
    }


    public static void getCells(String namespace, String tableName, String rowKey) throws IOException {
        // 1. 获取 table
        Connection connection = HBaseConnect.connection;
        Table table = connection.getTable(TableName.valueOf(namespace, tableName));
        // 2. 创建 get 对象
        Get get = new Get(Bytes.toBytes(rowKey));

        // 如果直接调用 get 方法读取数据 此时肚一整行数据
        // 如果想读取某一列的数据 需要添加对应的参数

        // 读取数据 得到 result 对象
        Result result = table.get(get);
        // 处理数据
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            // cell 存储数据比较底层
            String value = new String(CellUtil.cloneValue(cell));
            System.out.println(value);
        }

        // 关闭 table
        table.close();
    }

    public static void scanRows(String namespace, String tableName, String startRow, String stopRow) throws IOException {
        // 1. 获取 table
        Connection connection = HBaseConnect.connection;
        Table table = connection.getTable(TableName.valueOf(namespace, tableName));

        // 2. 创建 scan 对象
        Scan scan = new Scan();

        // 如果此时直接调用 会直接扫描整张表

        // 添加参数 来控制扫描的数据
        // 默认包含
        scan.withStartRow(Bytes.toBytes(startRow));
        // 默认不包含
        scan.withStopRow(Bytes.toBytes(stopRow));

        // 读取多行数据 或得 scanner
        ResultScanner scanner = table.getScanner(scan);
        // result 来记录一行数据 cell 数组
        // ResultScanner 来记录多行数据 result 的数组
        for (Result result : scanner) {
            Cell[] cells = result.rawCells();

            for (Cell cell : cells) {
                String value = new String(CellUtil.cloneValue(cell));
                System.out.println("value: " + value);
            }
        }
    }
}
