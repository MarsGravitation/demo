package com.microwu.cxd.netty.server.action.param;

import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface Param extends Map<String, Object> {

    /**
     * get String
     * @param param
     * @return
     */
    String getString(String param);

    /**
     * get Integer
     * @param param
     * @return
     */
    Integer getInteger(String param);

    /**
     * get Long
     * @param param
     * @return
     */
    Long getLong(String param);

    /**
     * get Double
     * @param param
     * @return
     */
    Double getDouble(String param);

    /**
     * get Float
     * @param param
     * @return
     */
    Float getFloat(String param);

    /**
     * get Boolean
     * @param param
     * @return
     */
    Boolean getBoolean(String param) ;
}