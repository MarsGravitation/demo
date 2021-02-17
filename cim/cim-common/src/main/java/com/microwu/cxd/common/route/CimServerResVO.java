package com.microwu.cxd.common.route;

import com.microwu.cxd.common.pojo.RouteInfo;
import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   14:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class CimServerResVO {

    private String ip;
    private Integer cimServerPort;
    private Integer httpPort;

    public CimServerResVO() {

    }

    public CimServerResVO(RouteInfo routeInfo) {
        this.ip = routeInfo.getIp();
        this.cimServerPort = routeInfo.getCimServerPort();
        this.httpPort = routeInfo.getHttpPort();
    }

}