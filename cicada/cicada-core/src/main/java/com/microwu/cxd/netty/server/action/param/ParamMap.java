package com.microwu.cxd.netty.server.action.param;

import java.util.HashMap;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ParamMap extends HashMap<String, Object> implements Param {
    @Override
    public String getString(String param) {
        return super.get(param).toString();
    }

    @Override
    public Integer getInteger(String param) {
        return Integer.parseInt(super.get(param).toString());
    }

    @Override
    public Long getLong(String param) {
        return (Long) super.get(param);
    }

    @Override
    public Double getDouble(String param) {
        return (Double) super.get(param);
    }

    @Override
    public Float getFloat(String param) {
        return (Float) super.get(param);
    }

    @Override
    public Boolean getBoolean(String param) {
        return (Boolean) super.get(param);
    }
}