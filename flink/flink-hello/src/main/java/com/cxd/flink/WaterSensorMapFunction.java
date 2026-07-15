package com.cxd.flink;

import org.apache.flink.api.common.functions.MapFunction;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/17   15:48
 * Update History:
 * Author        Time            Content
 */
public class WaterSensorMapFunction implements MapFunction<String, WaterSensor> {
    @Override
    public WaterSensor map(String s) throws Exception {
        String[] datas = s.split(",");
        return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
    }
}

