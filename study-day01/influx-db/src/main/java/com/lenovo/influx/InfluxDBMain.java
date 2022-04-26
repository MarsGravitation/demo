package com.lenovo.influx;

import org.influxdb.dto.QueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2022/4/18  15:01
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class InfluxDBMain {

    public static void main(String[] args) {
        insert();
    }

    public static void insert() {
        InfluxDBConnection influxDBConnection = new InfluxDBConnection("admin", "admin", "http://127.0.0.1:8086", "db-test", "hour");
        Map<String, String> tags = new HashMap<String, String>();
        tags.put("tag1", "标签值");
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("field1", "String类型");
        // 数值型，InfluxDB的字段类型，由第一天插入的值得类型决定
        fields.put("field2", 3.141592657);
        // 时间使用毫秒为单位
        influxDBConnection.insert("表名", tags, fields, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public static void query() {
        InfluxDBConnection influxDBConnection = new InfluxDBConnection("admin", "admin", "1.1.1.1", "db-test", "hour");
        QueryResult results = influxDBConnection
                .query("SELECT * FROM measurement where name = '大脑补丁'  order by time desc limit 1000");
        //results.getResults()是同时查询多条SQL语句的返回值，此处我们只有一条SQL，所以只取第一个结果集即可。
        QueryResult.Result oneResult = results.getResults().get(0);
        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues)
                    .collect(Collectors.toList()).get(0);
            if (valueList != null && valueList.size() > 0) {
                for (List<Object> value : valueList) {
                    Map<String, String> map = new HashMap<String, String>();
                    // 数据库中字段1取值
                    String field1 = value.get(0) == null ? null : value.get(0).toString();
                    // 数据库中字段2取值
                    String field2 = value.get(1) == null ? null : value.get(1).toString();
                    // TODO 用取出的字段做你自己的业务逻辑……
                }
            }
        }
    }

}
