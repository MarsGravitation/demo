package com.cxd.flink.function;

import com.cxd.flink.domain.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/2   14:11
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
