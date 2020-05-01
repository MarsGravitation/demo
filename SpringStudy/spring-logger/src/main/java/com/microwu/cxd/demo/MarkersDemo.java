package com.microwu.cxd.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 下面的用例可以通过添加MarkerFilters 仅允许记录SQL更新操作
 *
 * 规则：标记必须唯一，它们是通过名称永久注册的
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/20   14:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MarkersDemo {

    private Logger logger = LogManager.getLogger(MarkersDemo.class);
    private static final Marker SQL_MARKER = MarkerManager.getMarker("SQL");
    private static final Marker UPDATE_MARKER = MarkerManager.getMarker("SQL_UPDATE").setParents(SQL_MARKER);
    private static final Marker QUERY_MARKER = MarkerManager.getMarker("SQL_QUERY").setParents(SQL_MARKER);

    public String doQuery(String table) {
        logger.traceEntry();

        logger.info(QUERY_MARKER, "select * from {}", table);

        String result = "success";

        return logger.traceExit(result);
    }

    public String doUpdate(String table, Map<String, String> params) {
        logger.traceEntry();
        logger.info(UPDATE_MARKER, "update {} set {}", table, params);
        String result = "success";
        return logger.traceExit(result);
    }

    public static void main(String[] args) {

        for (int i = 0; i < 5000; i++) {
            MarkersDemo markersDemo = new MarkersDemo();
            markersDemo.doQuery("db_user");

            markersDemo.doUpdate("db_user", new HashMap<String, String>(16));
        }
    }

}