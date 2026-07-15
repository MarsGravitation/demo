package com.microwu.cxd.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   11:03
 * Update History:
 * Author        Time            Content
 */
public class WeatherTools {

    @Tool("返回给定城市的天气预报")
    public String getWeather(
            @P("应返回天气预报的城市") String city
    ) {
        return city + "的天气是晴";
    }
}
