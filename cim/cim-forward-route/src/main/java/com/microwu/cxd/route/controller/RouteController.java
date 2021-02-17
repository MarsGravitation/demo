package com.microwu.cxd.route.controller;

import com.microwu.cxd.common.req.NullBody;
import com.microwu.cxd.common.res.BaseResponse;
import com.microwu.cxd.common.route.ChatReqVO;
import com.microwu.cxd.route.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.microwu.cxd.common.enums.StatusEnum.SUCCESS;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   13:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @RequestMapping("/group")
    public BaseResponse<NullBody> group(@RequestBody ChatReqVO chatReqVO) {
        BaseResponse<NullBody> response = new BaseResponse<>();
        routeService.group(chatReqVO);
        response.setCode(SUCCESS.getCode());
        response.setMessage(SUCCESS.getMessage());
        return response;
    }

}