package com.microwu.cxd.common.pojo;

import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   15:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class RouteInfo {

    private String ip;
    private Integer cimServerPort;
    private Integer httpPort;

}